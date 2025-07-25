package com.conferent.controllers.auth;

import com.conferent.config.JwtTokenProvider;
import com.conferent.dtos.auth.LoginRequest;
import com.conferent.dtos.auth.LoginResponse;
import com.conferent.dtos.auth.RegisterRequest;
import com.conferent.dtos.user.UserResponse;
import com.conferent.entities.User;
import com.conferent.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인증 관련 API 컨트롤러
 * 
 * 제공 기능:
 * 1. 사용자 로그인 (JWT 토큰 발급)
 * 2. 사용자 회원가입
 * 3. 토큰 검증
 * 4. 현재 로그인 사용자 정보 조회
 * 
 * API 경로: /api/auth
 */
@Tag(name = "인증", description = "로그인, 회원가입, 토큰 관리 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    // JWT 토큰 생성/검증 담당 컴포넌트
    private final JwtTokenProvider jwtTokenProvider;
    
    // 사용자 관련 비즈니스 로직 담당 서비스
    private final UserService userService;

    /**
     * 사용자 로그인 API
     * 이메일과 비밀번호를 검증하고 JWT 토큰을 발급
     * 
     * @param loginRequest 로그인 요청 데이터 (이메일, 비밀번호)
     * @return 로그인 성공 시 JWT 토큰과 사용자 정보 반환
     */
    @PostMapping("/login")
    @Operation(summary = "사용자 로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 발급받습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공", 
            content = @Content(schema = @Schema(implementation = LoginResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패 - 이메일 또는 비밀번호 오류"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<?> login(
            @Parameter(description = "로그인 요청 정보")
            @Valid @RequestBody LoginRequest loginRequest) {
        
        try {
            log.info("로그인 시도 - 이메일: {}", loginRequest.getEmail());
            
            // 1. UserService를 통한 직접 사용자 인증
            boolean isAuthenticated = userService.authenticateUser(
                loginRequest.getEmail(), 
                loginRequest.getPassword()
            );
            
            if (!isAuthenticated) {
                log.warn("로그인 실패 - 잘못된 인증 정보: {}", loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("이메일 또는 비밀번호가 올바르지 않습니다.");
            }
            
            // 2. 인증 성공 시 사용자 정보 조회
            UserResponse user = userService.getUserByEmail(loginRequest.getEmail());
            
            // 3. JWT 토큰 생성 (사용자명 기반)
            String token = jwtTokenProvider.createToken(loginRequest.getEmail());
            
            // 4. 토큰 만료 시간 계산
            java.time.LocalDateTime expiresAt = jwtTokenProvider.getExpirationDate(token);
            
            // 5. 로그인 응답 객체 생성
            LoginResponse loginResponse = new LoginResponse(
                token,
                expiresAt,
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
            );
            
            log.info("로그인 성공 - 사용자: {}, 토큰 만료: {}", user.getEmail(), expiresAt);
            
            return ResponseEntity.ok(loginResponse);
            
        } catch (Exception e) {
            // 예상하지 못한 서버 오류
            log.error("로그인 중 서버 오류 발생: {}, 사용자: {}", e.getMessage(), loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("로그인 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 회원가입 API
     * 새로운 사용자 계정을 생성
     * 
     * @param registerRequest 회원가입 요청 데이터
     * @return 생성된 사용자 정보 반환
     */
    @PostMapping("/register")
    @Operation(summary = "사용자 회원가입", description = "새로운 사용자 계정을 생성합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 이메일"),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터")
    })
    public ResponseEntity<?> register(
            @Parameter(description = "회원가입 요청 정보")
            @Valid @RequestBody RegisterRequest registerRequest) {
        
        try {
            log.info("회원가입 시도 - 이메일: {}, 이름: {}", registerRequest.getEmail(), registerRequest.getName());
            
            // 1. 이메일 중복 체크 (UserService에서 처리됨)
            // 2. 새 사용자 생성 (비밀번호 암호화 포함)
            UserResponse newUser = userService.createUserWithEncryption(registerRequest);
            
            log.info("회원가입 성공 - 새 사용자 ID: {}, 이메일: {}", newUser.getId(), newUser.getEmail());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
            
        } catch (IllegalArgumentException e) {
            // 이메일 중복 등의 비즈니스 로직 오류
            log.warn("회원가입 실패 - {}: {}", e.getMessage(), registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
                    
        } catch (Exception e) {
            // 예상하지 못한 서버 오류
            log.error("회원가입 중 서버 오류 발생: {}, 사용자: {}", e.getMessage(), registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회원가입 처리 중 오류가 발생했습니다.");
        }
    }

    /**
     * 현재 로그인 사용자 정보 조회 API
     * JWT 토큰을 통해 현재 로그인한 사용자의 정보를 반환
     * 
     * @param authorization Authorization 헤더 (Bearer 토큰)
     * @return 현재 로그인 사용자 정보
     */
    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보", description = "현재 로그인한 사용자의 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
        @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<?> getCurrentUser(
            @Parameter(description = "JWT 토큰", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String authorization) {
        
        try {
            // 1. Authorization 헤더에서 토큰 추출
            String token = jwtTokenProvider.resolveToken(authorization);
            
            if (token == null) {
                log.warn("유효하지 않은 Authorization 헤더");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("유효하지 않은 토큰 형식입니다.");
            }
            
            // 2. 토큰 유효성 검증
            if (!jwtTokenProvider.validateToken(token)) {
                log.warn("유효하지 않은 JWT 토큰");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("유효하지 않은 토큰입니다.");
            }
            
            // 3. 토큰에서 사용자명(이메일) 추출
            String email = jwtTokenProvider.getUsername(token);
            
            // 4. 사용자 정보 조회
            UserResponse user = userService.getUserByEmail(email);
            
            log.debug("현재 사용자 정보 조회 성공 - 이메일: {}", email);
            
            return ResponseEntity.ok(user);
            
        } catch (Exception e) {
            log.error("현재 사용자 정보 조회 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("사용자 정보 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * JWT 토큰 검증 API
     * 클라이언트에서 토큰의 유효성을 확인할 때 사용
     * 
     * @param authorization Authorization 헤더 (Bearer 토큰)
     * @return 토큰 유효성 결과
     */
    @PostMapping("/validate")
    @Operation(summary = "토큰 검증", description = "JWT 토큰의 유효성을 검증합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유효한 토큰"),
        @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
    })
    public ResponseEntity<?> validateToken(
            @Parameter(description = "JWT 토큰", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String authorization) {
        
        try {
            // 1. Authorization 헤더에서 토큰 추출
            String token = jwtTokenProvider.resolveToken(authorization);
            
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("유효하지 않은 토큰 형식입니다.");
            }
            
            // 2. 토큰 유효성 검증
            boolean isValid = jwtTokenProvider.validateToken(token);
            
            if (isValid) {
                String email = jwtTokenProvider.getUsername(token);
                java.time.LocalDateTime expiresAt = jwtTokenProvider.getExpirationDate(token);
                
                return ResponseEntity.ok(new TokenValidationResponse(true, email, expiresAt));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new TokenValidationResponse(false, null, null));
            }
            
        } catch (Exception e) {
            log.error("토큰 검증 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("토큰 검증 중 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자 로그아웃 API
     * JWT 기반 인증에서는 서버 측에서 특별한 처리가 필요하지 않지만,
     * 클라이언트 요청을 처리하기 위한 엔드포인트
     * 
     * @param authorization Authorization 헤더 (Bearer 토큰)
     * @return 로그아웃 성공 응답
     */
    @PostMapping("/logout")
    @Operation(summary = "사용자 로그아웃", description = "사용자 로그아웃을 처리합니다. (JWT 토큰 무효화)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
        @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    public ResponseEntity<?> logout(
            @Parameter(description = "JWT 토큰", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        try {
            if (authorization != null) {
                String token = jwtTokenProvider.resolveToken(authorization);
                if (token != null && jwtTokenProvider.validateToken(token)) {
                    String email = jwtTokenProvider.getUsername(token);
                    log.info("로그아웃 처리 - 사용자: {}", email);
                }
            }
            
            // JWT 기반 인증에서는 서버 측에서 토큰을 무효화할 수 없으므로
            // 클라이언트에서 토큰을 삭제하도록 안내하는 응답
            return ResponseEntity.ok(new LogoutResponse(true, "로그아웃이 완료되었습니다."));
            
        } catch (Exception e) {
            log.error("로그아웃 처리 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LogoutResponse(false, "로그아웃 처리 중 오류가 발생했습니다."));
        }
    }

    /**
     * 로그아웃 응답 내부 클래스
     */
    @Schema(description = "로그아웃 응답")
    public static class LogoutResponse {
        @Schema(description = "로그아웃 성공 여부")
        public boolean success;
        
        @Schema(description = "응답 메시지")
        public String message;

        public LogoutResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }

    /**
     * 토큰 검증 응답 내부 클래스
     */
    @Schema(description = "토큰 검증 응답")
    public static class TokenValidationResponse {
        @Schema(description = "토큰 유효 여부")
        public boolean valid;
        
        @Schema(description = "토큰의 사용자 이메일")
        public String email;
        
        @Schema(description = "토큰 만료 시간")
        public java.time.LocalDateTime expiresAt;

        public TokenValidationResponse(boolean valid, String email, java.time.LocalDateTime expiresAt) {
            this.valid = valid;
            this.email = email;
            this.expiresAt = expiresAt;
        }
    }
} 
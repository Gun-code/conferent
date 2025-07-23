package com.conferent.dtos.auth;

import com.conferent.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 로그인 응답 DTO
 * 서버에서 로그인 성공 시 클라이언트에게 전송하는 데이터 구조
 * 
 * 포함 정보:
 * - JWT 토큰
 * - 토큰 타입
 * - 토큰 만료 시간
 * - 사용자 기본 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 응답")
public class LoginResponse {

    /**
     * JWT 액세스 토큰
     * 클라이언트가 API 요청 시 Authorization 헤더에 포함해야 하는 토큰
     */
    @Schema(description = "JWT 액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    /**
     * 토큰 타입
     * 일반적으로 "Bearer"로 고정
     */
    @Schema(description = "토큰 타입", example = "Bearer")
    private String tokenType = "Bearer";

    /**
     * 토큰 만료 시간
     * 클라이언트가 토큰 갱신 시점을 판단하는 데 사용
     */
    @Schema(description = "토큰 만료 시간", example = "2024-01-16T14:00:00")
    private LocalDateTime expiresAt;

    /**
     * 로그인한 사용자 ID
     */
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    /**
     * 로그인한 사용자 이름
     */
    @Schema(description = "사용자 이름", example = "홍길동")
    private String userName;

    /**
     * 로그인한 사용자 이메일
     */
    @Schema(description = "사용자 이메일", example = "hong@conferent.com")
    private String userEmail;

    /**
     * 로그인한 사용자 역할
     */
    @Schema(description = "사용자 역할", example = "USER")
    private Role userRole;

    /**
     * 편의 생성자
     * 필수 정보만으로 LoginResponse 객체 생성
     * 
     * @param accessToken JWT 토큰
     * @param expiresAt 만료 시간
     * @param userId 사용자 ID
     * @param userName 사용자 이름
     * @param userEmail 사용자 이메일
     * @param userRole 사용자 역할
     */
    public LoginResponse(String accessToken, LocalDateTime expiresAt, 
                        Long userId, String userName, String userEmail, Role userRole) {
        this.accessToken = accessToken;
        this.tokenType = "Bearer";
        this.expiresAt = expiresAt;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRole = userRole;
    }
} 
package com.conferent.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * JWT 인증 필터
 * 
 * 역할:
 * 1. HTTP 요청에서 JWT 토큰 추출
 * 2. 토큰 유효성 검증
 * 3. 유효한 토큰인 경우 Spring Security Context에 인증 정보 설정
 * 4. 무효한 토큰인 경우 인증 정보 제거
 * 
 * OncePerRequestFilter를 상속하여 요청당 한 번만 실행되도록 보장
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // JWT 토큰 처리를 담당하는 컴포넌트
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * HTTP 요청마다 실행되는 필터 메서드
     * JWT 토큰을 검증하고 인증 상태를 설정
     * 
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 예외
     * @throws IOException IO 예외
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, 
            HttpServletResponse response, 
            FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // 1. HTTP 요청에서 JWT 토큰 추출
            String token = extractTokenFromRequest(request);
            
            // 2. 토큰이 존재하고 유효한지 검증
            if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
                
                // 3. 토큰에서 사용자명(이메일) 추출
                String username = jwtTokenProvider.getUsername(token);
                
                if (username != null) {
                    // 4. Spring Security 인증 객체 생성
                    // 실제 애플리케이션에서는 데이터베이스에서 사용자 권한을 조회해야 함
                    // 여기서는 간단히 USER 권한을 부여
                    List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_USER")
                    );
                    
                    // 5. 인증 토큰 생성 (비밀번호는 null - 이미 JWT로 검증됨)
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(
                            username,           // 사용자명 (principal)
                            null,              // 비밀번호 (credentials) - JWT에서는 불필요
                            authorities        // 권한 목록
                        );
                    
                    // 6. 요청 세부 정보 설정 (IP 주소 등)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // 7. Spring Security Context에 인증 정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("JWT 인증 성공 - 사용자: {}, URI: {}", username, request.getRequestURI());
                }
            } else {
                // 토큰이 없거나 유효하지 않은 경우
                if (StringUtils.hasText(token)) {
                    log.debug("유효하지 않은 JWT 토큰 - URI: {}", request.getRequestURI());
                }
                
                // SecurityContext 초기화 (기존 인증 정보 제거)
                SecurityContextHolder.clearContext();
            }
            
        } catch (Exception e) {
            // JWT 처리 중 예외 발생 시 로그 기록 후 인증 정보 제거
            log.error("JWT 인증 처리 중 오류 발생 - URI: {}, 오류: {}", 
                     request.getRequestURI(), e.getMessage());
            
            SecurityContextHolder.clearContext();
            
            // 클라이언트에 인증 오류 응답 전송
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\": \"인증에 실패했습니다.\"}");
            return; // 필터 체인 중단
        }
        
        // 8. 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     * Authorization 헤더에서 "Bearer " 접두사를 제거하여 순수 토큰만 반환
     * 
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰 (토큰이 없으면 null)
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        // Authorization 헤더에서 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        
        // "Bearer " 접두사 확인 및 제거
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // "Bearer " 제거 (7글자)
            log.debug("JWT 토큰 추출 성공 - URI: {}", request.getRequestURI());
            return token;
        }
        
        // 토큰이 없거나 형식이 올바르지 않은 경우
        return null;
    }

    /**
     * 필터를 적용하지 않을 요청 경로 판별
     * 인증이 불필요한 공개 엔드포인트는 필터 처리를 건너뜀
     * 
     * @param request HTTP 요청
     * @return 필터 건너뛰기 여부 (true: 건너뛰기, false: 필터 적용)
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        
        // 인증이 불필요한 경로들
        String[] publicPaths = {
            "/api/auth/",           // 인증 관련 API
            "/api-docs",            // Swagger API 문서
            "/swagger-ui",          // Swagger UI
            "/h2-console",          // H2 데이터베이스 콘솔
            "/actuator/health",     // 헬스체크
            "/error"               // 에러 페이지
        };
        
        // 공개 경로에 해당하는지 검사
        for (String publicPath : publicPaths) {
            if (path.startsWith(publicPath)) {
                log.debug("공개 경로로 JWT 필터 건너뛰기 - URI: {}", path);
                return true;
            }
        }
        
        return false; // 필터 적용
    }
} 
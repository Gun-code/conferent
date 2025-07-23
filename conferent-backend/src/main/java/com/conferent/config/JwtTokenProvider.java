package com.conferent.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * JWT 토큰의 생성, 검증, 파싱을 담당하는 컴포넌트
 * 
 * 주요 기능:
 * 1. JWT 토큰 생성 (로그인 시)
 * 2. JWT 토큰 검증 (API 요청 시)
 * 3. 토큰에서 사용자 정보 추출
 * 4. 토큰 만료 시간 관리
 */
@Slf4j
@Component
public class JwtTokenProvider {

    // JWT 서명에 사용할 비밀키 (실제 운영환경에서는 환경변수나 외부 설정 파일에서 관리)
    private final SecretKey secretKey;
    
    // 토큰 만료 시간 (밀리초 단위, 기본값: 24시간)
    @Value("${jwt.token.validity:86400000}")
    private long tokenValidityInMilliseconds;

    /**
     * JwtTokenProvider 생성자
     * JWT 서명용 비밀키를 초기화
     * 
     * @param secretKeyString JWT 서명에 사용할 비밀키 문자열
     */
    public JwtTokenProvider(@Value("${jwt.secret:conferent-secret-key-for-jwt-token-signing-minimum-256-bits}") String secretKeyString) {
        // HMAC-SHA 알고리즘을 위한 충분히 긴 비밀키 생성
        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
        log.info("JWT Token Provider 초기화 완료");
    }

    /**
     * JWT 토큰 생성 메서드
     * 사용자 인증 정보를 바탕으로 JWT 토큰을 생성
     * 
     * @param authentication Spring Security의 인증 객체
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(Authentication authentication) {
        // 인증된 사용자 정보 가져오기
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        // 토큰 발급 시간과 만료 시간 계산
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
        
        log.debug("JWT 토큰 생성 - 사용자: {}, 만료시간: {}", username, validity);

        // JWT 토큰 빌드
        return Jwts.builder()
                .subject(username)                    // 토큰 주체 (사용자 식별자)
                .issuedAt(now)                       // 토큰 발급 시간
                .expiration(validity)                // 토큰 만료 시간
                .signWith(secretKey)                 // 서명 키로 토큰 서명
                .compact();                          // 최종 토큰 문자열 생성
    }

    /**
     * JWT 토큰 생성 (사용자명 기반)
     * 사용자명을 직접 받아서 토큰을 생성하는 오버로드 메서드
     * 
     * @param username 사용자명 (이메일)
     * @return 생성된 JWT 토큰 문자열
     */
    public String createToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + tokenValidityInMilliseconds);
        
        log.debug("JWT 토큰 생성 - 사용자: {}, 만료시간: {}", username, validity);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(validity)
                .signWith(secretKey)
                .compact();
    }

    /**
     * JWT 토큰에서 사용자명 추출
     * 
     * @param token JWT 토큰 문자열
     * @return 토큰에 포함된 사용자명
     */
    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)           // 서명 검증
                    .build()
                    .parseSignedClaims(token)        // 토큰 파싱
                    .getPayload();                   // 페이로드 추출
            
            return claims.getSubject();              // 사용자명 반환
        } catch (Exception e) {
            log.error("토큰에서 사용자명 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * JWT 토큰 유효성 검증
     * 토큰의 서명, 만료시간 등을 종합적으로 검증
     * 
     * @param token 검증할 JWT 토큰
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public boolean validateToken(String token) {
        try {
            // 토큰 파싱 시도 - 서명 검증과 만료시간 체크가 자동으로 수행됨
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            
            log.debug("JWT 토큰 검증 성공");
            return true;
            
        } catch (MalformedJwtException e) {
            log.error("잘못된 형식의 JWT 토큰: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("비어있는 JWT 토큰: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류 발생: {}", e.getMessage());
        }
        
        return false;
    }

    /**
     * JWT 토큰의 만료시간 추출
     * 
     * @param token JWT 토큰
     * @return 토큰 만료 시간 (LocalDateTime)
     */
    public LocalDateTime getExpirationDate(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            
            Date expiration = claims.getExpiration();
            return expiration.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            log.error("토큰 만료시간 추출 실패: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 토큰 만료 여부 확인
     * 
     * @param token JWT 토큰
     * @return 만료되었으면 true, 그렇지 않으면 false
     */
    public boolean isTokenExpired(String token) {
        try {
            LocalDateTime expiration = getExpirationDate(token);
            return expiration != null && expiration.isBefore(LocalDateTime.now());
        } catch (Exception e) {
            log.error("토큰 만료 확인 중 오류: {}", e.getMessage());
            return true; // 오류 발생 시 만료된 것으로 처리
        }
    }

    /**
     * HTTP 요청 헤더에서 Bearer 토큰 추출
     * Authorization: Bearer <token> 형식에서 토큰 부분만 추출
     * 
     * @param bearerToken Authorization 헤더 값
     * @return 추출된 JWT 토큰 (Bearer 접두사 제거됨)
     */
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거 (7글자)
        }
        return null;
    }
} 
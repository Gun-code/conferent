package com.conferent.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Security 설정 클래스
 * 
 * 주요 설정 내용:
 * 1. JWT 기반 인증 설정
 * 2. CORS 허용 설정
 * 3. 비밀번호 암호화 방식 설정
 * 4. 인증이 필요한/불필요한 엔드포인트 구분
 * 5. 세션 비사용 설정 (Stateless)
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize, @PostAuthorize 어노테이션 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    // JWT 토큰 처리를 위한 컴포넌트
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder Bean 등록
     * BCrypt 해시 알고리즘을 사용하여 비밀번호를 안전하게 암호화
     * 
     * BCrypt 특징:
     * - Salt 자동 생성
     * - 적응형 해시 함수 (계산 비용 조절 가능)
     * - Rainbow Table 공격에 강함
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("PasswordEncoder Bean 생성 - BCrypt 방식 사용");
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security의 AuthenticationManager Bean 등록
     * 로그인 시 사용자 인증을 담당
     * 
     * @param authConfig 인증 설정
     * @return AuthenticationManager 인스턴스
     * @throws Exception 설정 오류 시
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        log.info("AuthenticationManager Bean 생성");
        return authConfig.getAuthenticationManager();
    }

    /**
     * JWT 인증 필터 Bean 등록
     * HTTP 요청마다 JWT 토큰을 검증하여 사용자 인증 상태를 설정
     * 
     * @return JwtAuthenticationFilter 인스턴스
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        log.info("JwtAuthenticationFilter Bean 생성");
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /**
     * CORS 설정
     * 프론트엔드에서 백엔드 API 호출을 허용하기 위한 설정
     * 
     * @return CORS 설정 소스
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        log.info("CORS 설정 초기화");
        
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 허용할 Origin (프론트엔드 URL)
        configuration.setAllowedOriginPatterns(Arrays.asList(
            "http://localhost:3000",      // Vite 개발 서버
            "http://localhost:80",        // Nginx 개발 환경
            "https://conferent.com",      // 운영 환경
            "https://*.conferent.com"     // 서브도메인 포함
        ));
        
        // 허용할 HTTP 메서드
        configuration.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"
        ));
        
        // 허용할 헤더
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // 노출할 헤더 (클라이언트에서 접근 가능한 헤더)
        configuration.setExposedHeaders(Arrays.asList(
            "Access-Control-Allow-Origin",
            "Access-Control-Allow-Credentials"
        ));
        
        // 인증 정보(쿠키, Authorization 헤더 등) 허용
        configuration.setAllowCredentials(true);
        
        // preflight 요청 캐시 시간 (초)
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Spring Security 필터 체인 설정
     * HTTP 보안 설정의 핵심 부분
     * 
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain
     * @throws Exception 설정 오류 시
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("SecurityFilterChain 설정 초기화");
        
        http
            // CSRF 보호 비활성화 (JWT 사용 시 불필요)
            .csrf(AbstractHttpConfigurer::disable)
            
            // CORS 설정 적용
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // 세션 관리 설정 (JWT 사용으로 Stateless)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // URL별 인증 규칙 설정
            .authorizeHttpRequests(authz -> authz
                // 인증 불필요한 공개 엔드포인트
                .requestMatchers(
                    "/api/auth/**",           // 로그인, 회원가입
                    "/api-docs/**",           // Swagger API 문서
                    "/swagger-ui/**",         // Swagger UI
                    "/swagger-ui.html",       // Swagger UI 메인
                    "/h2-console/**",         // H2 데이터베이스 콘솔 (개발용)
                    "/actuator/health",       // 헬스체크
                    "/error"                  // 에러 페이지
                ).permitAll()
                
                // 관리자만 접근 가능한 엔드포인트
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // 그 외 모든 API는 인증 필요
                .anyRequest().authenticated()
            )
            
            // H2 콘솔 사용을 위한 설정 (개발환경에서만)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())  // H2 콘솔의 iframe 허용
            )
            
            // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        
        log.info("SecurityFilterChain 설정 완료");
        return http.build();
    }
} 
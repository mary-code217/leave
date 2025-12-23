package com.hoho.leave.config.security;

import com.hoho.leave.common.security.filter.CustomLogoutFilter;
import com.hoho.leave.common.security.filter.JWTFilter;
import com.hoho.leave.common.security.filter.LoginFilter;
import com.hoho.leave.common.security.jwt.JWTUtil;
import com.hoho.leave.domain.auth.RefreshRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Spring Security 설정 클래스.
 * 
 * JWT 기반의 Stateless 인증을 구성하고, 로그인/로그아웃 필터 및 권한 설정을 정의한다.
 * 
 */
@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshRepository refreshRepository;
    private final JWTUtil jwtUtil;

    /**
     * BCrypt 비밀번호 인코더를 빈으로 등록한다.
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager를 빈으로 등록한다.
     *
     * @param cfg 인증 설정
     * @return AuthenticationManager 인스턴스
     * @throws Exception 설정 실패 시
     */
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    /**
     * Security Filter Chain을 구성한다.
     * 
     * CSRF 비활성화, JWT 필터 등록, 세션 정책을 STATELESS로 설정한다.
     * 
     *
     * @param http HttpSecurity 객체
     * @return 구성된 SecurityFilterChain
     * @throws Exception 설정 실패 시
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.headers((headers) -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        http.authorizeHttpRequests((auth) -> auth
//                .requestMatchers("/login", "/", "/api/v1/user/join", "/logout").permitAll()
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                .requestMatchers("/api/**").hasRole("ADMIN")
//                .anyRequest().authenticated()
                        .requestMatchers("/**").permitAll()
        );

        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository),
                UsernamePasswordAuthenticationFilter.class);

        // JWT 필터 등록
        http.addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);

        // 로그아웃 필터 등록
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        // 세션 설정
        http.sessionManagement((session) -> session.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}

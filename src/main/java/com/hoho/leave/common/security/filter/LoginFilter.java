package com.hoho.leave.common.security.filter;

import com.hoho.leave.common.security.jwt.JWTUtil;
import com.hoho.leave.common.security.principal.CustomUserDetails;
import com.hoho.leave.domain.auth.RefreshEntity;
import com.hoho.leave.domain.auth.RefreshRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * 사용자 로그인을 처리하는 인증 필터.
 * <p>
 * POST /login 요청에서 이메일과 비밀번호를 받아 인증을 수행하고,
 * 성공 시 JWT 액세스 토큰과 리프레시 토큰을 발급한다.
 * </p>
 */
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    {
        setFilterProcessesUrl("/login");
        setUsernameParameter("email");
        setPasswordParameter("password");
    }

    /**
     * 사용자 인증을 시도한다.
     * <p>
     * 요청에서 이메일과 비밀번호를 추출하여 AuthenticationManager에 인증을 위임한다.
     * </p>
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @return 인증 결과
     * @throws AuthenticationException 인증 실패 시 발생
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        System.out.println("LoginFilter.attemptAuthentication 호출");

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(token);
    }

    /**
     * 인증 성공 시 호출되어 JWT 토큰을 발급한다.
     * <p>
     * 액세스 토큰은 응답 헤더에, 리프레시 토큰은 HttpOnly 쿠키에 설정한다.
     * 리프레시 토큰은 데이터베이스에도 저장된다.
     * </p>
     *
     * @param request        HTTP 요청
     * @param response       HTTP 응답
     * @param chain          필터 체인
     * @param authentication 인증 정보
     * @throws IOException      입출력 예외
     * @throws ServletException 서블릿 예외
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

        System.out.println("LoginFilter.successfulAuthentication 호출");

        //UserDetails
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        // 유저 정보
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String access = jwtUtil.createJwt("access", username, role, 600000L);
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        addRefreshEntity(username, refresh, 86400000L);

        //응답 설정
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 인증 실패 시 호출되어 401 응답을 반환한다.
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     * @param failed   인증 실패 예외
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        System.out.println("LoginFilter.unsuccessfulAuthentication 호출");

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(401);
    }

    /**
     * HttpOnly 쿠키를 생성한다.
     *
     * @param key   쿠키 이름
     * @param value 쿠키 값
     * @return 생성된 쿠키
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

        return cookie;
    }

    /**
     * 리프레시 토큰을 데이터베이스에 저장한다.
     *
     * @param username  사용자명
     * @param refresh   리프레시 토큰
     * @param expiredMs 만료 시간(밀리초)
     */
    private void addRefreshEntity(String username, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        RefreshEntity refreshEntity = new RefreshEntity(username, refresh, date.toString());

        refreshRepository.save(refreshEntity);
    }
}

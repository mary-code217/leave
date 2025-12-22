package com.hoho.leave.common.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 토큰의 생성 및 검증을 담당하는 유틸리티 클래스.
 * <p>
 * HS256 알고리즘을 사용하여 토큰을 서명하고 검증한다.
 * </p>
 */
@Component
public class JWTUtil {

    private final SecretKey secretKey;

    /**
     * JWTUtil을 생성한다.
     *
     * @param secret JWT 서명에 사용할 비밀키 문자열
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    /**
     * 토큰에서 사용자명을 추출한다.
     *
     * @param token JWT 토큰
     * @return 사용자명
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("username", String.class);
    }

    /**
     * 토큰에서 사용자 역할을 추출한다.
     *
     * @param token JWT 토큰
     * @return 사용자 역할
     */
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    /**
     * 토큰의 만료 여부를 확인한다.
     *
     * @param token JWT 토큰
     * @return 만료되었으면 true, 아니면 false
     */
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

    /**
     * 새로운 JWT 토큰을 생성한다.
     *
     * @param category  토큰 유형 (access 또는 refresh)
     * @param username  사용자명
     * @param role      사용자 역할
     * @param expiredMs 만료 시간(밀리초)
     * @return 생성된 JWT 토큰
     */
    public String createJwt(String category, String username, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category", category)
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }

    /**
     * 토큰에서 카테고리(유형)를 추출한다.
     *
     * @param token JWT 토큰
     * @return 토큰 카테고리 (access 또는 refresh)
     */
    public String getCategory(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("category", String.class);
    }

}

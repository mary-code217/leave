package com.hoho.leave.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * BCrypt 비밀번호 인코더 구현체.
 * <p>
 * BCrypt 알고리즘을 사용하여 비밀번호를 암호화하고 검증한다.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class BCryptEncoder implements PasswordEncoder{

    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 비밀번호를 BCrypt 알고리즘으로 암호화한다.
     *
     * @param password 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    @Override
    public String encode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 평문 비밀번호와 암호화된 비밀번호가 일치하는지 검증한다.
     *
     * @param password 평문 비밀번호
     * @param passwordHash 암호화된 비밀번호
     * @return 일치 여부
     */
    @Override
    public boolean matches(String password, String passwordHash) {
        return passwordEncoder.matches(password, passwordHash);
    }
}

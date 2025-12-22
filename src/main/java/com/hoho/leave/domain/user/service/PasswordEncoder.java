package com.hoho.leave.domain.user.service;

/**
 * 비밀번호 인코더 인터페이스.
 * <p>
 * 비밀번호 암호화 및 검증 기능을 정의한다.
 * </p>
 */
public interface PasswordEncoder {
    /**
     * 비밀번호를 암호화한다.
     *
     * @param password 평문 비밀번호
     * @return 암호화된 비밀번호
     */
    String encode(String password);

    /**
     * 평문 비밀번호와 암호화된 비밀번호가 일치하는지 검증한다.
     *
     * @param password 평문 비밀번호
     * @param passwordHash 암호화된 비밀번호
     * @return 일치 여부
     */
    boolean matches(String password, String passwordHash);
}

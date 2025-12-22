package com.hoho.leave.domain.auth;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 리프레시 토큰 저장 엔티티.
 * <p>
 * 사용자의 리프레시 토큰을 데이터베이스에 저장하여
 * 토큰 갱신 및 로그아웃 시 검증에 사용한다.
 * </p>
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 사용자 이메일 */
    private String userEmail;

    /** 리프레시 토큰 값 */
    private String refresh;

    /** 만료 일시 */
    private String expiration;

    /**
     * 리프레시 토큰 엔티티를 생성한다.
     *
     * @param userEmail  사용자 이메일
     * @param refresh    리프레시 토큰
     * @param expiration 만료 일시
     */
    public RefreshEntity(String userEmail, String refresh, String expiration) {
        this.userEmail = userEmail;
        this.refresh = refresh;
        this.expiration = expiration;
    }
}

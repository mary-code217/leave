package com.hoho.leave.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 리프레시 토큰 데이터 접근 레포지토리.
 */
public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {

    /**
     * 리프레시 토큰 존재 여부를 확인한다.
     *
     * @param refresh 리프레시 토큰
     * @return 존재하면 true, 아니면 false
     */
    Boolean existsByRefresh(String refresh);

    /**
     * 리프레시 토큰을 삭제한다.
     *
     * @param refresh 삭제할 리프레시 토큰
     */
    @Transactional
    void deleteByRefresh(String refresh);
}

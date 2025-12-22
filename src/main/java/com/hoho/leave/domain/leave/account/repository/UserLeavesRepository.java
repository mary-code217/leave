package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 휴가 계정 리포지토리.
 * <p>
 * 휴가 계정 엔티티에 대한 데이터베이스 접근을 제공한다.
 * </p>
 */
public interface UserLeavesRepository extends JpaRepository<UserLeaves, Long> {
    /**
     * 특정 사용자의 휴가 계정 존재 여부를 확인한다.
     *
     * @param userId 사용자 ID
     * @return 존재 여부
     */
    boolean existsByUserId(Long userId);

    /**
     * 특정 사용자의 휴가 계정을 조회한다.
     * 사용자 정보를 함께 조회하여 N+1 문제를 방지한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 휴가 계정
     */
    @EntityGraph(attributePaths = {"user"})
    Optional<UserLeaves> findByUserId(Long userId);
}

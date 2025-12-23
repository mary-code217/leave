package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.UserDefaultApprover;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 기본 결재자 리포지토리.
 * 
 * 사용자 기본 결재자 엔티티에 대한 데이터베이스 접근을 담당한다.
 * 
 */
public interface UserDefaultApproverRepository extends JpaRepository<UserDefaultApprover, Long> {
    /**
     * 사용자와 결재자 조합의 존재 여부를 확인한다.
     *
     * @param userId 사용자 ID
     * @param approverId 결재자 ID
     * @return 존재 여부
     */
    boolean existsByUserIdAndApproverId(Long userId, Long approverId);
}

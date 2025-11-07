package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.UserDefaultApprover;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDefaultApproverRepository extends JpaRepository<UserDefaultApprover, Long> {
    boolean existsByUserIdAndApproverId(Long userId, Long approverId);
}

package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLeavesRepository extends JpaRepository<UserLeaves, Long> {
    boolean existsByUserId(Long userId);
}

package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    boolean existsByLeaveName(String leaveName);
}

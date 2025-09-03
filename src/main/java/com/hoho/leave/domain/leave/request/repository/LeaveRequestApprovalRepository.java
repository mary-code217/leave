package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeaveRequestApprovalRepository extends JpaRepository<LeaveRequestApproval, Long> {
}

package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveRequestApprovalRepository extends JpaRepository<LeaveRequestApproval, Long> {
    @EntityGraph(attributePaths = {"leaveRequest", "leaveRequest.applicant"})
    Optional<LeaveRequestApproval> findById(Long id);

    @EntityGraph(attributePaths = {"leaveRequest", "leaveRequest.applicant"})
    Page<LeaveRequestApproval> findByApproverId(Long approverId, Pageable pageable);
}

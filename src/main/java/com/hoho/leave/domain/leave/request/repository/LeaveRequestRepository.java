package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Page<LeaveRequest> findByUserId(Long userId, Pageable pageable);
}

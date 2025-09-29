package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<LeaveRequestAttachment, Long> {
    @EntityGraph(attributePaths = {"uploadedBy"})
    List<LeaveRequestAttachment> findByLeaveRequestId(Long leaveRequestId);
}

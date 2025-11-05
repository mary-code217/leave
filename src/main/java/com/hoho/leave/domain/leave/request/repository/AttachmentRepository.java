package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<LeaveRequestAttachment, Long> {
    @EntityGraph(attributePaths = {"uploadedBy"})
    List<LeaveRequestAttachment> findByLeaveRequestId(Long leaveRequestId);

    @Query("""
       select a.leaveRequest.id as reqId, count(a.id) as cnt
       from LeaveRequestAttachment a
       where a.leaveRequest.id in :ids
       group by a.leaveRequest.id
    """)
    List<ReqIdCount> countByLeaveRequestIds(@Param("ids") List<Long> ids);

    interface ReqIdCount {
        Long getReqId();
        long getCnt();
    }
}

package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Page<LeaveRequest> findByUserId(Long userId, Pageable pageable);

    @Query("""
        SELECT lr
        FROM LeaveRequest lr
        JOIN FETCH lr.leaveType
        JOIN FETCH lr.user
        WHERE lr.id = :leaveRequestId
    """)
    Optional<LeaveRequest> findByIdWithUserAndLeaveType(Long leaveRequestId);

    @EntityGraph(attributePaths = {"user", "leaveType"})
    Page<LeaveRequest> findAll(Pageable pageable);

    @Query("""
        select count(distinct r.user.id)
          from LeaveRequest r
         where r.user.team.id = :teamId
           and r.leaveType.id = :leaveTypeId
           and r.status in ('APPROVED', 'PENDING_APPROVAL')
           and not (r.endDay   < :from or r.startDay > :to)
        """)
    long countConcurrentRequests(
            @Param("teamId") Long teamId,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}

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

/**
 * 휴가 신청 리포지토리.
 * <p>
 * 휴가 신청 정보의 데이터 접근을 담당한다.
 * </p>
 */
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    /**
     * 특정 사용자의 휴가 신청 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param pageable 페이지 정보
     * @return 휴가 신청 페이지
     */
    Page<LeaveRequest> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자와 휴가 유형 정보를 포함하여 휴가 신청을 조회한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 휴가 신청
     */
    @Query("""
        SELECT lr
        FROM LeaveRequest lr
        JOIN FETCH lr.leaveType
        JOIN FETCH lr.user
        WHERE lr.id = :leaveRequestId
    """)
    Optional<LeaveRequest> findByIdWithUserAndLeaveType(Long leaveRequestId);

    /**
     * 모든 휴가 신청 목록을 조회한다.
     *
     * @param pageable 페이지 정보
     * @return 휴가 신청 페이지
     */
    @EntityGraph(attributePaths = {"user", "leaveType"})
    Page<LeaveRequest> findAll(Pageable pageable);

    /**
     * 특정 기간 동안 같은 팀에서 같은 휴가 유형을 사용하는 동시 신청 수를 계산한다.
     *
     * @param teamId 팀 ID
     * @param leaveTypeId 휴가 유형 ID
     * @param from 시작일
     * @param to 종료일
     * @return 동시 신청 수
     */
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

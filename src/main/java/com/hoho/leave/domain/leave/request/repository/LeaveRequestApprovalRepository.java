package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 휴가 결재 리포지토리.
 * 
 * 휴가 결재 정보의 데이터 접근을 담당한다.
 * 
 */
public interface LeaveRequestApprovalRepository extends JpaRepository<LeaveRequestApproval, Long> {
    /**
     * ID로 휴가 결재를 조회한다.
     *
     * @param id 결재 ID
     * @return 휴가 결재
     */
    @EntityGraph(attributePaths = {"leaveRequest", "leaveRequest.applicant"})
    Optional<LeaveRequestApproval> findById(Long id);

    /**
     * 특정 결재자의 결재 목록을 조회한다.
     *
     * @param approverId 결재자 ID
     * @param pageable 페이지 정보
     * @return 휴가 결재 페이지
     */
    @EntityGraph(attributePaths = {"leaveRequest", "leaveRequest.applicant"})
    Page<LeaveRequestApproval> findByApproverId(Long approverId, Pageable pageable);
}

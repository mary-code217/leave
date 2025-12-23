package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.leave.request.dto.request.LeaveApprovalUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalListResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestApprovalRepository;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 휴가 결재 서비스.
 * 
 * 휴가 신청의 결재 승인, 반려 등 결재 관련 비즈니스 로직을 처리한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class LeaveRequestApprovalService {

    private final LeaveRequestApprovalRepository approvalRepository;

    /**
     * 휴가 결재를 생성한다.
     *
     * @param leaveRequest 휴가 신청
     * @param Approver 결재자
     * @param stepNo 결재 단계
     * @return 생성된 휴가 결재
     */
    @Transactional
    public LeaveRequestApproval createLeaveApproval(
            LeaveRequest leaveRequest, User Approver, Integer stepNo) {

        return approvalRepository.save(LeaveRequestApproval.create(leaveRequest, Approver, stepNo));
    }

    /**
     * 휴가 결재 정보를 수정한다.
     *
     * @param approvalId 결재 ID
     * @param updateRequest 결재 수정 요청
     */
    @Transactional
    public void updateLeaveApproval(Long approvalId, LeaveApprovalUpdateRequest updateRequest) {
        LeaveRequestApproval leaveRequestApproval = getLeaveRequestApprovalEntity(approvalId);

        leaveRequestApproval.update(updateRequest.getStatus(), updateRequest.getComment());
    }

    /**
     * 특정 휴가 결재를 조회한다.
     *
     * @param leaveApprovalId 결재 ID
     * @return 휴가 결재 응답
     */
    @Transactional(readOnly = true)
    public LeaveApprovalResponse getLeaveApproval(Long leaveApprovalId) {
        LeaveRequestApproval leaveRequestApproval = getLeaveRequestApprovalEntity(leaveApprovalId);

        return LeaveApprovalResponse.of(leaveRequestApproval);
    }

    /**
     * 특정 결재자의 모든 휴가 결재 목록을 조회한다.
     *
     * @param approverId 결재자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 결재 목록 응답
     */
    @Transactional(readOnly = true)
    public LeaveApprovalListResponse getAllLeaveApproval(Long approverId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);

        Page<LeaveRequestApproval> pageList = approvalRepository.findByApproverId(approverId, pageable);

        List<LeaveApprovalResponse> list = pageList.getContent().stream()
                .map(LeaveApprovalResponse::of)
                .toList();

        return LeaveApprovalListResponse.of(pageList, list);
    }

    /**
     * 페이지 요청 정보를 생성한다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 페이지 요청 정보
     */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("createdAt")));
    }

    /**
     * 휴가 결재 엔티티를 조회한다.
     *
     * @param approvalId 결재 ID
     * @return 휴가 결재 엔티티
     */
    private LeaveRequestApproval getLeaveRequestApprovalEntity(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .orElseThrow(() -> new NotFoundException("Not Found LeaveRequestApproval : " + approvalId));
    }
}

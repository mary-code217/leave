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

@Service
@RequiredArgsConstructor
public class LeaveRequestApprovalService {

    private final LeaveRequestApprovalRepository approvalRepository;

    @Transactional
    public LeaveRequestApproval createLeaveApproval(
            LeaveRequest leaveRequest, User Approver, Integer stepNo) {

        return approvalRepository.save(LeaveRequestApproval.create(leaveRequest, Approver, stepNo));
    }

    @Transactional
    public void updateLeaveApproval(Long approvalId, LeaveApprovalUpdateRequest updateRequest) {
        LeaveRequestApproval leaveRequestApproval = getLeaveRequestApprovalEntity(approvalId);

        leaveRequestApproval.update(updateRequest.getStatus(), updateRequest.getComment());
    }

    @Transactional(readOnly = true)
    public LeaveApprovalResponse getLeaveApproval(Long leaveApprovalId) {
        LeaveRequestApproval leaveRequestApproval = getLeaveRequestApprovalEntity(leaveApprovalId);

        return LeaveApprovalResponse.of(leaveRequestApproval);
    }

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
     * 페이지 요청 정보 생성
     */
    private static PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("createdAt")));
    }

    /**
     * 휴가 신청 결재 내역 조회
     */
    private LeaveRequestApproval getLeaveRequestApprovalEntity(Long approvalId) {
        return approvalRepository.findById(approvalId)
                .orElseThrow(() -> new NotFoundException("Not Found LeaveRequestApproval : " + approvalId));
    }
}

package com.hoho.leave.domain.leave.request.service;

import com.hoho.leave.domain.leave.request.dto.request.LeaveApprovalUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalListResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import com.hoho.leave.domain.leave.request.repository.LeaveRequestApprovalRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.util.exception.BusinessException;
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

    public LeaveRequestApproval createLeaveApproval(
            LeaveRequest leaveRequest, User Approver, Integer stepNo) {

        return approvalRepository.save(LeaveRequestApproval.create(leaveRequest, Approver, stepNo));
    }

    @Transactional
    public void updateLeaveApproval(Long approvalId, LeaveApprovalUpdateRequest updateRequest) {
        LeaveRequestApproval leaveRequestApproval = approvalRepository.findById(approvalId)
                .orElseThrow(() -> new BusinessException("변경 실패 - 존재하지 않는 결재내역 입니다."));

        leaveRequestApproval.update(updateRequest.getStatus(), updateRequest.getComment());
    }

    @Transactional(readOnly = true)
    public LeaveApprovalResponse getLeaveApproval(Long leaveApprovalId) {
        LeaveRequestApproval leaveRequestApproval = approvalRepository.findById(leaveApprovalId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 결재내역 입니다."));

        return LeaveApprovalResponse.of(
                leaveApprovalId,
                leaveRequestApproval.getStatus(),
                leaveRequestApproval.getLeaveRequest().getUser().getId(),
                leaveRequestApproval.getLeaveRequest().getUser().getUsername(),
                leaveRequestApproval.getLeaveRequest().getQuantityDays(),
                leaveRequestApproval.getLeaveRequest().getStartDay(),
                leaveRequestApproval.getLeaveRequest().getEndDay(),
                leaveRequestApproval.getLeaveRequest().getStartTime(),
                leaveRequestApproval.getLeaveRequest().getEndTime()
        );
    }

    @Transactional(readOnly = true)
    public LeaveApprovalListResponse getAllLeaveApproval(Long approverId, Integer page, Integer size) {

        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.asc("createdAt")));

        Page<LeaveRequestApproval> pageList = approvalRepository.findByApproverId(approverId, pageable);

        List<LeaveApprovalResponse> list = pageList.getContent().stream()
                .map(a ->  LeaveApprovalResponse.of(
                        a.getId(),
                        a.getStatus(),
                        a.getLeaveRequest().getUser().getId(),
                        a.getLeaveRequest().getUser().getUsername(),
                        a.getLeaveRequest().getQuantityDays(),
                        a.getLeaveRequest().getStartDay(),
                        a.getLeaveRequest().getEndDay(),
                        a.getLeaveRequest().getStartTime(),
                        a.getLeaveRequest().getEndTime()
                ))
                .toList();


        return LeaveApprovalListResponse.of(
                page,
                size,
                list,
                pageList.getTotalPages(),
                pageList.getTotalElements(),
                pageList.isFirst(),
                pageList.isLast()
        );
    }
}

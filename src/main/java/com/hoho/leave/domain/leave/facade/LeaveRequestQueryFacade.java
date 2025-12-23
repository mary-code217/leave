package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.leave.request.dto.response.AttachmentResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestDetailResponse;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.service.AttachmentService;
import com.hoho.leave.domain.leave.request.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 휴가 신청 조회 파사드.
 * 
 * 휴가 신청 조회 시 여러 도메인 서비스를 조율한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class LeaveRequestQueryFacade {

    private final LeaveRequestService leaveRequestService;
    private final AttachmentService attachmentService;

    /**
     * 휴가 신청 상세 정보를 조회한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 휴가 신청 상세 응답
     */
    @Transactional(readOnly = true)
    public LeaveRequestDetailResponse getLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestService.getRequestEntityWithUserAndType(leaveRequestId);
        List<AttachmentResponse> attachments = attachmentService.getAttachments(leaveRequestId);

        LeaveRequestDetailResponse response = LeaveRequestDetailResponse.of(leaveRequest);
        response.addAttachments(attachments);

        return response;
    }
}

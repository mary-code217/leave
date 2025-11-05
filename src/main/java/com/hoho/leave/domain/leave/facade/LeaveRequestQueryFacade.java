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

@Service
@RequiredArgsConstructor
public class LeaveRequestQueryFacade {

    private final LeaveRequestService leaveRequestService;
    private final AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public LeaveRequestDetailResponse getLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestService.getRequestEntityWithUserAndType(leaveRequestId);
        List<AttachmentResponse> attachments = attachmentService.getAttachments(leaveRequestId);

        LeaveRequestDetailResponse response = LeaveRequestDetailResponse.of(leaveRequest);
        response.addAttachments(attachments);

        return response;
    }
}

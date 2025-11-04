package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class LeaveRequestDetailResponse {

    Long leaveRequestId;

    LocalDate startDay;

    LocalDate endDay;

    LocalTime startTime;

    LocalTime endTime;

    LeaveRequestStatus status;

    Long userId;

    String username;

    Long leaveTypeId;

    String leaveTypeName;

    List<AttachmentResponse> attachments;

    public static LeaveRequestDetailResponse of(LeaveRequest leaveRequest, List<AttachmentResponse> attachments) {
        LeaveRequestDetailResponse response = new LeaveRequestDetailResponse();

        response.leaveRequestId = leaveRequest.getId();
        response.startDay = leaveRequest.getStartDay();
        response.endDay = leaveRequest.getEndDay();
        response.startTime = leaveRequest.getStartTime();
        response.endTime = leaveRequest.getEndTime();
        response.status = leaveRequest.getStatus();

        response.userId = leaveRequest.getUser().getId();
        response.username = leaveRequest.getUser().getUsername();

        response.leaveTypeId = leaveRequest.getLeaveType().getId();
        response.leaveTypeName = leaveRequest.getLeaveType().getLeaveName();

        response.attachments = attachments;

        return response;
    }
}

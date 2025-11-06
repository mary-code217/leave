package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.ApprovalStatus;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LeaveApprovalResponse {
    Long approvalId;

    ApprovalStatus status;

    Long authorId;

    String authorName;

    BigDecimal quantityDays;

    LocalDate startDay;

    LocalDate endDay;

    LocalTime startTime;

    LocalTime endTime;

    public static LeaveApprovalResponse of(LeaveRequestApproval request) {
        LeaveApprovalResponse response = new LeaveApprovalResponse();

        response.approvalId = request.getId();
        response.status = request.getStatus();
        response.authorId = request.getLeaveRequest().getUser().getId();
        response.authorName = request.getLeaveRequest().getUser().getUsername();
        response.quantityDays = request.getLeaveRequest().getQuantityDays();
        response.startDay = request.getLeaveRequest().getStartDay();
        response.endDay = request.getLeaveRequest().getEndDay();
        response.startTime = request.getLeaveRequest().getStartTime();
        response.endTime = request.getLeaveRequest().getEndTime();

        return response;
    }
}

package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestDetailResponse {

    Long leaveRequestId;
    Long userId;
    String username;
    Long leaveTypeId;
    String leaveTypeName;
    LocalDate startDay;
    LocalDate endDay;
    LocalTime startTime;
    LocalTime endTime;
    LeaveRequestStatus status;

    public LeaveRequestDetailResponse(Long leaveRequestId,
                                      LocalDate startDay, LocalDate endDay,
                                      LocalTime startTime, LocalTime endTime,
                                      LeaveRequestStatus status) {
        this.leaveRequestId = leaveRequestId;
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    public static LeaveRequestDetailResponse from(LeaveRequest leaveRequest) {
        return new LeaveRequestDetailResponse(
            leaveRequest.getId(),
            leaveRequest.getStartDay(),
            leaveRequest.getEndDay(),
            leaveRequest.getStartTime(),
            leaveRequest.getEndTime(),
            leaveRequest.getStatus()
        );
    }

    public void addUser(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
    }

    public void addLeaveType(LeaveType leaveType) {
        this.leaveTypeId = leaveType.getId();
        this.leaveTypeName = leaveType.getLeaveName();
    }
}

package com.hoho.leave.domain.leave.policy.dto.response;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeaveTypeDetailResponse {
    Long leaveTypeId;

    String leaveTypeName;

    String leaveCode;

    private BigDecimal unitDays;

    boolean leaveDecrement = true;

    boolean requiresAttachment = false;

    private String leaveDescription;

    public static LeaveTypeDetailResponse of(LeaveType leaveType) {
        LeaveTypeDetailResponse response = new LeaveTypeDetailResponse();

        response.leaveTypeId = leaveType.getId();
        response.leaveTypeName = leaveType.getLeaveName();
        response.leaveCode = leaveType.getLeaveCode().toString();
        response.unitDays = leaveType.getUnitDays();
        response.leaveDecrement = leaveType.isLeaveDecrement();
        response.requiresAttachment = leaveType.isRequiresAttachment();
        response.leaveDescription = leaveType.getLeaveDescription();

        return response;
    }
}

package com.hoho.leave.domain.leave.policy.dto.response;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeDetailResponse {
    Long leaveTypeId;
    String leaveTypeName;
    String leaveCode;
    private BigDecimal unitDays;
    boolean leaveDecrement = true;
    boolean requiresAttachment = false;
    private String leaveDescription;

    public static LeaveTypeDetailResponse from(LeaveType leaveType) {
        return new LeaveTypeDetailResponse(
                leaveType.getId(),
                leaveType.getLeaveName(),
                leaveType.getLeaveCode().toString(),
                leaveType.getUnitDays(),
                leaveType.isLeaveDecrement(),
                leaveType.isRequiresAttachment(),
                leaveType.getLeaveDescription()
        );
    }
}

package com.hoho.leave.domain.leave.policy.dto.response;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 휴가 유형 상세 응답 DTO.
 * 
 * 휴가 유형의 상세 정보를 반환하는 응답 데이터를 담는다.
 * 
 */
@Data
public class LeaveTypeDetailResponse {
    Long leaveTypeId;

    String leaveTypeName;

    String leaveCode;

    private BigDecimal unitDays;

    boolean leaveDecrement = true;

    boolean requiresAttachment = false;

    private String leaveDescription;

    /**
     * LeaveType 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param leaveType 휴가 유형 엔티티
     * @return 휴가 유형 상세 응답 DTO
     */
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

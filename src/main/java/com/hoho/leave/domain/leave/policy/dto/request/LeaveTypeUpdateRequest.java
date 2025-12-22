package com.hoho.leave.domain.leave.policy.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 휴가 유형 수정 요청 DTO.
 * <p>
 * 기존 휴가 유형을 수정하기 위한 요청 데이터를 담는다.
 * </p>
 */
@Data
public class LeaveTypeUpdateRequest {
    @NotBlank
    String leaveTypeName;

    @NotBlank
    String leaveCode;

    @Digits(integer = 3, fraction = 2)
    private BigDecimal unitDays;

    boolean leaveDecrement = true;

    boolean requiresAttachment = false;

    private String leaveDescription;

}

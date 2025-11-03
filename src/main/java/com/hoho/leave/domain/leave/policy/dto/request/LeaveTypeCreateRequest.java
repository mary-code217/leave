package com.hoho.leave.domain.leave.policy.dto.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeaveTypeCreateRequest {
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

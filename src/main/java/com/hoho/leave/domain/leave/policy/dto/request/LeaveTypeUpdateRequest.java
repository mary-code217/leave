package com.hoho.leave.domain.leave.policy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LeaveTypeUpdateRequest {

    @NotBlank
    @NotNull
    String leaveTypeName;

    @NotBlank @NotNull
    String leaveCode;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    @DecimalMin(value = "0.01")
    private BigDecimal unitDays;

    boolean leaveDecrement = true;

    boolean requiresAttachment = false;

    private String leaveDescription;

}

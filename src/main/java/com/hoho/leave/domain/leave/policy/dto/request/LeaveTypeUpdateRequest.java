package com.hoho.leave.domain.leave.policy.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

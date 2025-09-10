package com.hoho.leave.domain.leave.request.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestCreateRequest {

    @NotNull
    Long userId;
    @NotNull
    Long leaveTypeId;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    @DecimalMin(value = "0.01")
    BigDecimal quantityDays;

    @NotBlank
    LocalDate startDay;
    LocalDate endDay;
    LocalTime startTime;
    LocalTime endTime;
}

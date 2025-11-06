package com.hoho.leave.domain.leave.request.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LeaveRequestCreateRequest {
    @NotNull
    Long userId;

    @NotNull
    Long leaveTypeId;

    @Digits(integer = 3, fraction = 2)
    BigDecimal quantityDays;

    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDay;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDay;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalTime endTime;
}

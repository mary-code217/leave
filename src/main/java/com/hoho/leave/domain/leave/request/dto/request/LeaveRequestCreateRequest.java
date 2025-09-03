package com.hoho.leave.domain.leave.request.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveRequestCreateRequest {

    @NotNull @NotBlank
    Long userId;
    @NotNull @NotBlank
    Long leaveTypeId;
    @NotNull @NotBlank
    LocalDate startDay;

    LocalDate endDay;
    LocalTime startTime;
    LocalTime endTime;
}

package com.hoho.leave.domain.leave.policy.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveConcurrencyPolicyRequest {
    @NotNull
    Long teamId;

    @NotNull
    Long leaveTypeId;

    @NotNull @Min(0)
    Integer maxConcurrent;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate effectiveFrom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate effectiveTo;
}
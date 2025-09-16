package com.hoho.leave.domain.leave.policy.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
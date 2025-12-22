package com.hoho.leave.domain.leave.policy.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 휴가 동시 제한 정책 생성 요청 DTO.
 * <p>
 * 팀별, 휴가 유형별 동시 휴가 제한 정책을 생성하기 위한 요청 데이터를 담는다.
 * </p>
 */
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
package com.hoho.leave.domain.leave.request.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 휴가 신청 생성 요청 DTO.
 * 
 * 새로운 휴가를 신청할 때 필요한 정보를 담는다.
 * 
 */
@Data
public class LeaveRequestCreateRequest {
    /**
     * 사용자 ID
     */
    @NotNull
    Long userId;

    /**
     * 휴가 유형 ID
     */
    @NotNull
    Long leaveTypeId;

    /**
     * 사용 일수
     */
    @Digits(integer = 3, fraction = 2)
    BigDecimal quantityDays;

    /**
     * 시작일
     */
    @NotBlank
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startDay;

    /**
     * 종료일
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endDay;

    /**
     * 시작 시간
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalTime startTime;

    /**
     * 종료 시간
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalTime endTime;
}

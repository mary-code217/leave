package com.hoho.leave.domain.leave.holiday.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * 공휴일 생성 요청 DTO.
 * 
 * 공휴일 날짜와 명칭 정보를 담는다.
 * 
 */
@Data
public class HolidayCreateRequest {
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @NotBlank
    String name;
}

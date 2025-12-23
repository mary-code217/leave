package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.LocalDate;

/**
 * 공휴일 상세 응답 DTO.
 * 
 * 공휴일 날짜와 명칭 정보를 담는다.
 * 
 */
@Data
public class HolidayDetailResponse {
    LocalDate holiday;

    String holidayName;

    /**
     * 공휴일 상세 응답을 생성한다.
     *
     * @param holiday 공휴일 날짜
     * @param holidayName 공휴일 명칭
     * @return 공휴일 상세 응답
     */
    public static HolidayDetailResponse of(LocalDate holiday, String holidayName) {
        HolidayDetailResponse response = new HolidayDetailResponse();

        response.holiday = holiday;
        response.holidayName = holidayName;

        return response;
    }
}

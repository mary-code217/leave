package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * 기간별 공휴일 응답 DTO.
 * 
 * 특정 기간의 공휴일 목록 정보를 담는다.
 * 
 */
@Data
public class HolidayRangeResponse {
    LocalDate startDate;

    LocalDate endDate;

    List<HolidayDetailResponse> monthList;

    /**
     * 기간별 공휴일 응답을 생성한다.
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @param monthList 공휴일 목록
     * @return 기간별 공휴일 응답
     */
    public static HolidayRangeResponse of(LocalDate startDate, LocalDate endDate, List<HolidayDetailResponse> monthList) {
        HolidayRangeResponse response = new HolidayRangeResponse();

        response.startDate = startDate;
        response.endDate = endDate;
        response.monthList = monthList;

        return response;
    }
}

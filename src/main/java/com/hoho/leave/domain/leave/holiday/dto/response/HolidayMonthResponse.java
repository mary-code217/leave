package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.YearMonth;
import java.util.List;

/**
 * 월별 공휴일 응답 DTO.
 * <p>
 * 특정 월의 공휴일 목록 정보를 담는다.
 * </p>
 */
@Data
public class HolidayMonthResponse {
    YearMonth yearMonth;

    List<HolidayDetailResponse> monthList;

    /**
     * 월별 공휴일 응답을 생성한다.
     *
     * @param yearMonth 조회 년월
     * @param monthList 공휴일 목록
     * @return 월별 공휴일 응답
     */
    public static HolidayMonthResponse of(YearMonth yearMonth, List<HolidayDetailResponse> monthList) {
        HolidayMonthResponse response = new HolidayMonthResponse();

        response.yearMonth = yearMonth;
        response.monthList = monthList;

        return response;
    }
}

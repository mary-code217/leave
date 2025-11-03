package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.YearMonth;
import java.util.List;

@Data
public class HolidayMonthResponse {
    YearMonth yearMonth;

    List<HolidayDetailResponse> monthList;

    public static HolidayMonthResponse of(YearMonth yearMonth, List<HolidayDetailResponse> monthList) {
        HolidayMonthResponse response = new HolidayMonthResponse();

        response.yearMonth = yearMonth;
        response.monthList = monthList;

        return response;
    }
}

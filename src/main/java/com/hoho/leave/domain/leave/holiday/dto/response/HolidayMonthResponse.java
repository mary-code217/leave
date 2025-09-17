package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayMonthResponse {
    YearMonth yearMonth;
    List<HolidayDetailResponse> monthList;

    public static HolidayMonthResponse of(YearMonth yearMonth, List<HolidayDetailResponse> monthList) {
        return HolidayMonthResponse.builder()
                .yearMonth(yearMonth)
                .monthList(monthList).build();
    }
}

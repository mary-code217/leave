package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayRangeResponse {
    LocalDate startDate;
    LocalDate endDate;
    List<HolidayDetailResponse> monthList;

    public static HolidayRangeResponse of(LocalDate startDate, LocalDate endDate, List<HolidayDetailResponse> monthList) {
        return HolidayRangeResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .monthList(monthList).build();
    }
}

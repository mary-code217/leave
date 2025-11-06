package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class HolidayRangeResponse {
    LocalDate startDate;

    LocalDate endDate;

    List<HolidayDetailResponse> monthList;

    public static HolidayRangeResponse of(LocalDate startDate, LocalDate endDate, List<HolidayDetailResponse> monthList) {
        HolidayRangeResponse response = new HolidayRangeResponse();

        response.startDate = startDate;
        response.endDate = endDate;
        response.monthList = monthList;

        return response;
    }
}

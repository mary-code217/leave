package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HolidayDetailResponse {
    LocalDate holiday;

    String holidayName;

    public static HolidayDetailResponse of(LocalDate holiday, String holidayName) {
        HolidayDetailResponse response = new HolidayDetailResponse();

        response.holiday = holiday;
        response.holidayName = holidayName;

        return response;
    }
}

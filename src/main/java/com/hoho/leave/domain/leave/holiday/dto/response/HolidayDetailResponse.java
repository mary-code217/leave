package com.hoho.leave.domain.leave.holiday.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HolidayDetailResponse {
    LocalDate holiday;
    String holidayName;

    public static HolidayDetailResponse of(LocalDate holiday, String holidayName) {
        return HolidayDetailResponse.builder()
                .holiday(holiday)
                .holidayName(holidayName).build();
    }
}

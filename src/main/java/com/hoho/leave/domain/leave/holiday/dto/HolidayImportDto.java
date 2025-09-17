package com.hoho.leave.domain.leave.holiday.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayImportDto {
    LocalDate holidayDate;
    String holidayName;
}

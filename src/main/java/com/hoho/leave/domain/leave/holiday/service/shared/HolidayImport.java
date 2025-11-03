package com.hoho.leave.domain.leave.holiday.service.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayImport {
    LocalDate holidayDate;
    String holidayName;
}

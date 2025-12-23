package com.hoho.leave.domain.leave.holiday.service.shared;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 공휴일 가져오기 DTO.
 * 
 * API로부터 가져온 공휴일 정보를 담는다.
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HolidayImport {
    LocalDate holidayDate;
    String holidayName;
}

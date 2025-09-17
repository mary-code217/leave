package com.hoho.leave.domain.leave.holiday.service;

import com.hoho.leave.domain.leave.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

}

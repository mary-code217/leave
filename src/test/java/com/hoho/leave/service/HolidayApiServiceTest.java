package com.hoho.leave.service;

import com.hoho.leave.domain.leave.holiday.service.HolidayApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class HolidayApiServiceTest {

    @Autowired
    HolidayApiService holidayApiService;

    @Test
    public void API_호출_테스트() {
        var list = holidayApiService.fetchRestHolidaysByMonth(2025, 10);
        list.forEach(a -> System.out.println(a.getHolidayDate() +" : "+ a.getHolidayName()));
    }
}

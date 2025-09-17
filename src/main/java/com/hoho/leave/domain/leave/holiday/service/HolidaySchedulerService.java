package com.hoho.leave.domain.leave.holiday.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolidaySchedulerService {

    @Scheduled(cron = "0 10 00,14 * * *", zone = "Asia/Seoul")
    public void schedule() {

    }

}

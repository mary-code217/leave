package com.hoho.leave.domain.leave.holiday.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class HolidaySchedulerService {

    private final HolidaySyncService holidaySyncService;

    @Scheduled(cron = "0 10 00,14 * * *", zone = "Asia/Seoul")
    public void HolidaySchedule() {
        YearMonth start = YearMonth.now().minusMonths(3);
        YearMonth end = YearMonth.now().plusMonths(3);

        for (YearMonth ym = start; !ym.isAfter(end); ym = ym.plusMonths(1)) {
            try {
                holidaySyncService.syncHoliday(ym.getYear(), ym.getMonthValue());
            } catch (Exception e) {
                log.warn("Holiday sync failed for {}: {}", ym, e.getMessage());
            }
        }
    }
}

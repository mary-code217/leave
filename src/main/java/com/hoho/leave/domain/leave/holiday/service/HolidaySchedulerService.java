package com.hoho.leave.domain.leave.holiday.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.YearMonth;

/**
 * 공휴일 스케줄러 서비스.
 * <p>
 * 정기적으로 공휴일 정보를 동기화한다.
 * </p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HolidaySchedulerService {

    private final HolidaySyncService holidaySyncService;

    /**
     * 공휴일 정보를 정기적으로 동기화한다.
     * 매일 00시 10분, 14시 10분에 현재 기준 3개월 전후의 공휴일을 동기화한다.
     */
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

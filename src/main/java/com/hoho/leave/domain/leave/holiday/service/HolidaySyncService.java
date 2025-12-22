package com.hoho.leave.domain.leave.holiday.service;

import com.hoho.leave.domain.leave.holiday.service.shared.HolidayImport;
import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import com.hoho.leave.domain.leave.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 공휴일 동기화 서비스.
 * <p>
 * 공공데이터 포털 API로부터 공휴일 정보를 가져와 데이터베이스와 동기화한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HolidaySyncService {

    private final HolidayRepository holidayRepository;
    private final HolidayApiService holidayApiService;

    /**
     * 특정 년월의 공휴일 정보를 동기화한다.
     *
     * @param year 년도
     * @param month 월
     */
    @Transactional
    public void syncHoliday(Integer year, Integer month) {
        List<HolidayImport> apiList = holidayApiService.fetchRestHolidaysByMonth(year, month);

        apiList.forEach(h -> {
            holidayRepository.findByHolidayDate(h.getHolidayDate()).ifPresentOrElse(
                    before -> renameIfChanged(before, h.getHolidayName()),
                    () -> holidayRepository.save(Holiday.create(h.getHolidayDate(), normalizeName(h.getHolidayName())))
            );
        });
    }

    /**
     * 공휴일 명칭이 변경된 경우 업데이트한다.
     *
     * @param before 기존 공휴일 엔티티
     * @param afterName 새로운 명칭
     */
    public void renameIfChanged(Holiday before, String afterName) {
        if(!normalizeName(before.getHolidayName()).equals(normalizeName(afterName))) {
            before.rename(normalizeName(afterName));
        }
    }

    /**
     * 공휴일 명칭을 정규화한다.
     *
     * @param name 원본 명칭
     * @return 정규화된 명칭
     */
    public String normalizeName(String name) {
        if (name == null) return "";
        return name.replaceAll("\\s+", " ").trim();
    }
}

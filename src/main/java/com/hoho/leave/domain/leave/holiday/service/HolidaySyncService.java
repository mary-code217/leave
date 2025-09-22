package com.hoho.leave.domain.leave.holiday.service;

import com.hoho.leave.domain.leave.holiday.dto.HolidayImportDto;
import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import com.hoho.leave.domain.leave.holiday.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidaySyncService {

    private final HolidayRepository holidayRepository;
    private final HolidayApiService holidayApiService;

    @Transactional
    public void syncHoliday(Integer year, Integer month) {
        List<HolidayImportDto> apiList = holidayApiService.fetchRestHolidaysByMonth(year, month);

        apiList.forEach(h -> {
            holidayRepository.findByHolidayDate(h.getHolidayDate()).ifPresentOrElse(
                    before -> renameIfChanged(before, h.getHolidayName()),
                    () -> holidayRepository.save(Holiday.create(h.getHolidayDate(), normalizeName(h.getHolidayName())))
            );
        });
    }

    public void renameIfChanged(Holiday before, String afterName) {
        if(!normalizeName(before.getHolidayName()).equals(normalizeName(afterName))) {
            before.rename(normalizeName(afterName));
        }
    }

    public String normalizeName(String name) {
        if (name == null) return "";
        return name.replaceAll("\\s+", " ").trim();
    }
}

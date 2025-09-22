package com.hoho.leave.domain.leave.holiday.service;

import com.hoho.leave.domain.leave.holiday.dto.request.HolidayCreateRequest;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayDetailResponse;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayMonthResponse;
import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import com.hoho.leave.domain.leave.holiday.repository.HolidayRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public void createHoliday(HolidayCreateRequest request) {
        String name = normalizeName(request.getName());

        holidayRepository.findByHolidayDateAndHolidayName(request.getDate(), name).ifPresentOrElse(
                h -> { throw new BusinessException("이미 존재하는 공휴일 입니다."); },
                () -> holidayRepository.save(Holiday.create(request.getDate(), name))
        );
    }

    // 공휴일 가져오는 메서드 (특정 달)
    public HolidayMonthResponse getAllHolidayMonth(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Holiday> findList = holidayRepository.findAllByHolidayDateBetween(start, end);
        List<HolidayDetailResponse> list = findList.stream().map(a -> HolidayDetailResponse.of(a.getHolidayDate(), a.getHolidayName())).toList();

        return HolidayMonthResponse.of(month, list);
    }

    // 공휴일 가져오는 메서드 (범위)

    public String normalizeName(String name) {
        if (name == null) return "";
        return name.replaceAll("\\s+", " ").trim();
    }
}

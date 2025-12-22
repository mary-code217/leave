package com.hoho.leave.domain.leave.holiday.service;

import com.hoho.leave.domain.leave.holiday.dto.request.HolidayCreateRequest;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayDetailResponse;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayMonthResponse;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayRangeResponse;
import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import com.hoho.leave.domain.leave.holiday.repository.HolidayRepository;
import com.hoho.leave.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * 공휴일 서비스.
 * <p>
 * 공휴일의 생성, 조회 기능을 제공한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    /**
     * 공휴일을 생성한다.
     *
     * @param request 공휴일 생성 요청
     */
    @Transactional
    public void createHoliday(HolidayCreateRequest request) {
        String name = normalizeName(request.getName());

        holidayRepository.findByHolidayDateAndHolidayName(request.getDate(), name).ifPresentOrElse(
                h -> { throw new BusinessException("이미 존재하는 공휴일 입니다."); },
                () -> holidayRepository.save(Holiday.create(request.getDate(), name))
        );
    }

    /**
     * 특정 월의 공휴일 목록을 조회한다.
     *
     * @param month 조회할 년월
     * @return 월별 공휴일 응답
     */
    @Transactional(readOnly = true)
    public HolidayMonthResponse getAllHolidayMonth(YearMonth month) {
        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        List<Holiday> findList = holidayRepository.findAllByHolidayDateBetween(start, end);
        List<HolidayDetailResponse> list = findList.stream().map(a -> HolidayDetailResponse.of(a.getHolidayDate(), a.getHolidayName())).toList();

        return HolidayMonthResponse.of(month, list);
    }

    /**
     * 특정 기간의 공휴일 목록을 조회한다.
     *
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 기간별 공휴일 응답
     */
    public HolidayRangeResponse getRangeHoliday(LocalDate startDate, LocalDate endDate) {

        List<Holiday> findList = holidayRepository.findAllByHolidayDateBetween(startDate, endDate);
        List<HolidayDetailResponse> list = findList.stream().map(a -> HolidayDetailResponse.of(a.getHolidayDate(), a.getHolidayName())).toList();

        return HolidayRangeResponse.of(startDate, endDate, list);
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

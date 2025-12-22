package com.hoho.leave.domain.leave.holiday.controller;

import com.hoho.leave.domain.leave.holiday.dto.request.HolidayCreateRequest;
import com.hoho.leave.domain.leave.holiday.dto.response.HolidayMonthResponse;
import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import com.hoho.leave.domain.leave.holiday.service.HolidayService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

/**
 * 공휴일 컨트롤러.
 * <p>
 * 공휴일 생성, 조회 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/holiday")
public class HolidayController {

    private final HolidayService holidayService;

    /**
     * 공휴일을 생성한다.
     *
     * @param request 공휴일 생성 요청
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createHoliday(@RequestBody @Valid HolidayCreateRequest request) {

        holidayService.createHoliday(request);

        return ResponseEntity.status(HttpStatus.OK).body("공휴일 생성 성공");
    }

    /**
     * 특정 월의 공휴일 목록을 조회한다.
     *
     * @param ym 조회할 년월
     * @return 월별 공휴일 목록 응답
     */
    @GetMapping("/{ym}")
    public ResponseEntity<HolidayMonthResponse> getAllHolidayMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth ym) {

        HolidayMonthResponse response = holidayService.getAllHolidayMonth(ym);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/holiday")
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping("")
    public ResponseEntity<?> createHoliday(@RequestBody @Valid HolidayCreateRequest request) {

        holidayService.createHoliday(request);

        return ResponseEntity.status(HttpStatus.OK).body("공휴일 생성 성공");
    }

    @GetMapping("/{ym}")
    public ResponseEntity<HolidayMonthResponse> getAllHolidayMonth(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth ym) {

        HolidayMonthResponse response = holidayService.getAllHolidayMonth(ym);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

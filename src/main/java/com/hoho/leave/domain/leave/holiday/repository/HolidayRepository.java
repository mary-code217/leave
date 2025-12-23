package com.hoho.leave.domain.leave.holiday.repository;

import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * 공휴일 리포지토리.
 * 
 * 공휴일 엔티티에 대한 데이터베이스 접근을 제공한다.
 * 
 */
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    /**
     * 특정 날짜의 공휴일을 조회한다.
     *
     * @param holidayDate 공휴일 날짜
     * @return 공휴일 엔티티
     */
    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);

    /**
     * 특정 날짜와 명칭의 공휴일을 조회한다.
     *
     * @param date 공휴일 날짜
     * @param holidayName 공휴일 명칭
     * @return 공휴일 엔티티
     */
    Optional<Holiday> findByHolidayDateAndHolidayName(LocalDate date, String holidayName);

    /**
     * 특정 기간의 모든 공휴일을 조회한다.
     *
     * @param startInclusive 시작일 (포함)
     * @param endInclusive 종료일 (포함)
     * @return 공휴일 목록
     */
    List<Holiday> findAllByHolidayDateBetween(LocalDate startInclusive, LocalDate endInclusive);
}

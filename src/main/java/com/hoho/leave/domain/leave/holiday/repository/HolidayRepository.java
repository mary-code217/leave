package com.hoho.leave.domain.leave.holiday.repository;

import com.hoho.leave.domain.leave.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    Optional<Holiday> findByHolidayDate(LocalDate holidayDate);
}

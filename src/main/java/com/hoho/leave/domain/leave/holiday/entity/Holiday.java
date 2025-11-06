package com.hoho.leave.domain.leave.holiday.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "holiday",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_holiday_date", columnNames = "holiday_date")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false)
    LocalDate holidayDate;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    public static Holiday create(LocalDate holidayDate, String holidayName) {
        Holiday holiday = new Holiday();

        holiday.holidayDate = holidayDate;
        holiday.holidayName = holidayName;

        return holiday;
    }

    public void rename(String holidayName) {
        this.holidayName = holidayName;
    }
}

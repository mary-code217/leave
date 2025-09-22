package com.hoho.leave.domain.leave.holiday.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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
@NoArgsConstructor
public class Holiday extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "holiday_date", nullable = false)
    LocalDate holidayDate;

    @Column(name = "holiday_name", nullable = false)
    private String holidayName;

    @Builder(access = AccessLevel.PRIVATE)
    public Holiday(LocalDate holidayDate, String holidayName) {
        this.holidayDate = holidayDate;
        this.holidayName = holidayName;
    }

    public static Holiday create(LocalDate holidayDate, String holidayName) {
        return Holiday.builder()
                .holidayDate(holidayDate)
                .holidayName(holidayName)
                .build();
    }

    public void rename(String holidayName) {
        this.holidayName = holidayName;
    }
}

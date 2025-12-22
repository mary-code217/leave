package com.hoho.leave.domain.leave.holiday.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 공휴일 엔티티.
 * <p>
 * 공휴일 날짜와 명칭을 관리한다.
 * </p>
 */
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

    /**
     * 공휴일을 생성한다.
     *
     * @param holidayDate 공휴일 날짜
     * @param holidayName 공휴일 명칭
     * @return 생성된 공휴일 엔티티
     */
    public static Holiday create(LocalDate holidayDate, String holidayName) {
        Holiday holiday = new Holiday();

        holiday.holidayDate = holidayDate;
        holiday.holidayName = holidayName;

        return holiday;
    }

    /**
     * 공휴일 명칭을 변경한다.
     *
     * @param holidayName 새로운 공휴일 명칭
     */
    public void rename(String holidayName) {
        this.holidayName = holidayName;
    }
}

package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.domain.leave.account.service.support.AccrualSchedule;
import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 사용자 휴가 계정 엔티티.
 * 
 * 사용자의 휴가 잔여 일수와 휴가 부여 정보를 관리한다.
 * 
 */
@Entity
@Getter
@Table(name = "user_leaves")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLeaves extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_stage", nullable = false)
    private LeaveStage leaveStage = LeaveStage.NONE;

    @Column(name = "next_accrual_at")
    private LocalDate nextAccrualAt;

    @Column(name = "balance_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal balanceDays = new BigDecimal("0.00");

    /**
     * 사용자 휴가 계정을 생성한다.
     *
     * @param user 사용자
     * @param schedule 휴가 부여 스케줄
     * @param balanceDays 초기 잔여 일수
     * @return 생성된 사용자 휴가 계정
     */
    public static UserLeaves create(User user, AccrualSchedule schedule, BigDecimal balanceDays) {
        UserLeaves userLeaves = new UserLeaves();

        userLeaves.user = user;
        userLeaves.leaveStage = schedule.getLeaveStage();
        userLeaves.nextAccrualAt = schedule.getNextAccrualAt();
        userLeaves.balanceDays = balanceDays;

        return userLeaves;
    }

    /**
     * 휴가 잔여 일수를 업데이트한다.
     *
     * @param newBalanceDays 새로운 잔여 일수
     */
    public void updateBalanceDays(BigDecimal newBalanceDays) {
        this.balanceDays = newBalanceDays;
    }
}


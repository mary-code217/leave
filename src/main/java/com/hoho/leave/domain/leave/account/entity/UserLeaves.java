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

@Entity
@Getter
@Table(
        name = "user_leaves",
        uniqueConstraints = {
                // 유저×휴가유형 1행만 존재
                @UniqueConstraint(name = "uq_user_leaves_user_type", columnNames = {"user_id", "leave_type_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLeaves extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    public static UserLeaves create(User user, AccrualSchedule schedule, BigDecimal balanceDays) {
        UserLeaves userLeaves = new UserLeaves();

        userLeaves.user = user;
        userLeaves.leaveStage = schedule.getLeaveStage();
        userLeaves.nextAccrualAt = schedule.getNextAccrualAt();
        userLeaves.balanceDays = balanceDays;

        return userLeaves;
    }
}


package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.policy.entity.LeaveStage;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

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
public class UserLeaves extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK 필수
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // FK 필수
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    // 단계: NONE/MONTHLY/ANNUAL
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_stage", nullable = false)
    private LeaveStage leaveStage = LeaveStage.NONE;

    // 월 적립 포스팅 횟수(음수 금지)
    @Column(name = "monthly_grants_posted", nullable = false)
    private Integer monthlyGrantsPosted = 0;

    @Column(name = "next_accrual_at")
    private LocalDate nextAccrualAt;

    // 회사 정책상 상한 25일 고정
    @Column(name = "cap_days", nullable = false)
    private Integer capDays = 25;

    // 잔액 일수(소수 2자리, 음수 금지)
    @Column(name = "balance_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal balanceDays = new BigDecimal("0.00");
}


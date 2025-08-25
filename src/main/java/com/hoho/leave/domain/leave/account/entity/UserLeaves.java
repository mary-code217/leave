package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.leave.policy.entity.LeaveStage;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Table(name = "user_leaves")
public class UserLeaves extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @Enumerated(EnumType.STRING)
    private LeaveStage leaveStage = LeaveStage.NONE;

    @Column(name = "monthly_grants_posted")
    private Integer monthlyGrantsPosted = 0;

    @Column(name = "next_accrual_at")
    private LocalDate nextAccrualAt;

    @Column(name = "cap_days", nullable = false)
    private Integer capDays = 25;

    @Column(name = "balance_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal balanceDays = new BigDecimal("0.00");

    @PrePersist
    void prePersist() {
        this.capDays = 25;
    }
}

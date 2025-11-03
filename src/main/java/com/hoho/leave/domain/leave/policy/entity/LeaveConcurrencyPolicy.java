package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.domain.leave.policy.service.support.ConcurrencyPolicyParams;
import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.entity.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "leave_concurrency_policy",
        uniqueConstraints = {
                // 같은 팀+휴가유형에서 같은 시작일 정책 중복 금지
                @UniqueConstraint(name = "uq_lcp_team_type_from",
                        columnNames = {"team_id", "leave_type_id", "effective_from"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeaveConcurrencyPolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "max_concurrent", nullable = false)
    private Integer maxConcurrent;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    public static LeaveConcurrencyPolicy create(ConcurrencyPolicyParams request) {
        LeaveConcurrencyPolicy concurrencyPolicy = new LeaveConcurrencyPolicy();

        concurrencyPolicy.team = request.getTeam();
        concurrencyPolicy.leaveType = request.getLeaveType();
        concurrencyPolicy.maxConcurrent = request.getMaxConcurrent();
        concurrencyPolicy.effectiveFrom = request.getEffectiveFrom();
        concurrencyPolicy.effectiveTo = request.getEffectiveTo();

        return concurrencyPolicy;
    }

    public boolean isEffectiveOn(LocalDate date) {
        if (date == null) return false;
        boolean fromOk = !date.isBefore(effectiveFrom);
        boolean toOk = (effectiveTo == null) || !date.isAfter(effectiveTo);
        return fromOk && toOk;
    }
}


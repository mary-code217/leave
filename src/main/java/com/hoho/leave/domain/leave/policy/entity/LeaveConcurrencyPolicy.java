package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.domain.leave.policy.service.support.ConcurrencyPolicyParams;
import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.org.entity.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 휴가 동시 제한 정책 엔티티.
 * 
 * 팀별, 휴가 유형별 동시 사용 가능 인원수를 제한하는 정책을 관리한다.
 * 
 */
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

    /**
     * 휴가 동시 제한 정책을 생성한다.
     *
     * @param request 정책 생성 파라미터
     * @return 생성된 정책 엔티티
     */
    public static LeaveConcurrencyPolicy create(ConcurrencyPolicyParams request) {
        LeaveConcurrencyPolicy concurrencyPolicy = new LeaveConcurrencyPolicy();

        concurrencyPolicy.team = request.getTeam();
        concurrencyPolicy.leaveType = request.getLeaveType();
        concurrencyPolicy.maxConcurrent = request.getMaxConcurrent();
        concurrencyPolicy.effectiveFrom = request.getEffectiveFrom();
        concurrencyPolicy.effectiveTo = request.getEffectiveTo();

        return concurrencyPolicy;
    }

    /**
     * 특정 날짜에 정책이 유효한지 확인한다.
     *
     * @param date 확인할 날짜
     * @return 유효 여부
     */
    public boolean isEffectiveOn(LocalDate date) {
        if (date == null) return false;
        boolean fromOk = !date.isBefore(effectiveFrom);
        boolean toOk = (effectiveTo == null) || !date.isAfter(effectiveTo);
        return fromOk && toOk;
    }
}


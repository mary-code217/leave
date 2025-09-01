package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.org.entity.Team;
import jakarta.persistence.*;
import lombok.Getter;

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
public class LeaveConcurrencyPolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 대상 팀 (필수)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    // 대상 휴가 유형 (필수)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    // 동시에 휴가 가능한 최대 인원(0=전면 금지 허용), null 금지
    @Column(name = "max_concurrent", nullable = false)
    private Integer maxConcurrent;

    // 적용 시작일(필수)
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    // 적용 종료일(선택; null=무기한)
    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    // 유효일자 판단 (도메인 편의)
    public boolean isEffectiveOn(LocalDate date) {
        if (date == null) return false;
        boolean fromOk = !date.isBefore(effectiveFrom);
        boolean toOk = (effectiveTo == null) || !date.isAfter(effectiveTo);
        return fromOk && toOk;
    }
}


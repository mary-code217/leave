package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "leave_type_policy",
        uniqueConstraints = {
                // 한 leave_type 안에서 동일 leave_code 중복 금지
                @UniqueConstraint(name = "uq_ltp_type_code", columnNames = {"leave_type_id", "leave_code"})
        }
)
public class LeaveTypePolicy extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 휴가유형의 정책인지 (FK 필수)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    // 차감 여부 (기본 true 권장)  — Boolean(래퍼)보다 boolean이 안전
    @Column(name = "leave_decrement", nullable = false)
    private boolean leaveDecrement = true;

    // 증빙(첨부) 필수 여부 (기본 false 권장)
    @Column(name = "requires_attachment", nullable = false)
    private boolean requiresAttachment = false;

    // 사용 코드: DAY / HALF_DAY_AM / HALF_DAY_PM / QUARTER_DAY ...
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_code", nullable = false)
    private LeaveCode leaveCode;
}


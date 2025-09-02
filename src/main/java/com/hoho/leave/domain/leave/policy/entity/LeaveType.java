package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@Table(
        name = "leave_type",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_leave_type_name", columnNames = "leave_name")
        }
)
@AllArgsConstructor
@NoArgsConstructor
public class LeaveType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 휴가명: 전사 유일 + NOT NULL
    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    @Column(name = "unit_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal unitDays;

    // 차감 여부 (기본 true 권장)
    @Column(name = "leave_decrement", nullable = false)
    private boolean leaveDecrement = true;

    // 증빙(첨부) 필수 여부 (기본 false 권장)
    @Column(name = "requires_attachment", nullable = false)
    private boolean requiresAttachment = false;

    // 사용 코드: DAY / HALF_DAY_AM / HALF_DAY_PM / QUARTER_DAY ...
    @Enumerated(EnumType.STRING)
    @Column(name = "leave_code", nullable = false)
    private LeaveCode leaveCode;

    // 설명
    @Column(name = "leave_description")
    private String leaveDescription;

    public LeaveType(String leaveName, BigDecimal unitDays, boolean leaveDecrement, boolean requiresAttachment, LeaveCode leaveCode, String leaveDescription) {
        this.leaveName = leaveName;
        this.unitDays = unitDays;
        this.leaveDecrement = leaveDecrement;
        this.requiresAttachment = requiresAttachment;
        this.leaveCode = leaveCode;
        this.leaveDescription = leaveDescription;
    }

    public static LeaveType Create(LeaveTypeCreateRequest req) {
        return new LeaveType(
                req.getLeaveTypeName(),
                req.getUnitDays(),
                req.isLeaveDecrement(),
                req.isRequiresAttachment(),
                LeaveCode.valueOf(req.getLeaveCode()),
                req.getLeaveDescription()
        );
    }

    public void Update(LeaveTypeUpdateRequest req) {
        this.leaveName = req.getLeaveTypeName();
        this.unitDays = req.getUnitDays();
        this.leaveDecrement = req.isLeaveDecrement();
        this.requiresAttachment = req.isRequiresAttachment();
        this.leaveCode = LeaveCode.valueOf(req.getLeaveCode());
        this.leaveDescription = req.getLeaveDescription();
    }
}

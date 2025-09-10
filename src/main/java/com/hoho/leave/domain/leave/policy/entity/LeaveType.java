package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@NoArgsConstructor
public class LeaveType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "leave_name", nullable = false)
    private String leaveName;

    @Column(name = "unit_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal unitDays;

    @Column(name = "leave_decrement", nullable = false)
    private boolean leaveDecrement = true;

    @Column(name = "requires_attachment", nullable = false)
    private boolean requiresAttachment = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_code", nullable = false)
    private LeaveCode leaveCode;

    @Column(name = "leave_description")
    private String leaveDescription;

    @Builder
    public LeaveType(String leaveName, BigDecimal unitDays, boolean leaveDecrement, boolean requiresAttachment, LeaveCode leaveCode, String leaveDescription) {
        this.leaveName = leaveName;
        this.unitDays = unitDays;
        this.leaveDecrement = leaveDecrement;
        this.requiresAttachment = requiresAttachment;
        this.leaveCode = leaveCode;
        this.leaveDescription = leaveDescription;
    }

    public static LeaveType Create(LeaveTypeCreateRequest req) {
        return LeaveType.builder()
                .leaveName(req.getLeaveTypeName())
                .unitDays(req.getUnitDays())
                .leaveDecrement(req.isLeaveDecrement())
                .requiresAttachment(req.isRequiresAttachment())
                .leaveCode(LeaveCode.valueOf(req.getLeaveCode()))
                .leaveDescription(req.getLeaveDescription())
                .build();
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

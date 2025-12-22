package com.hoho.leave.domain.leave.policy.entity;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 휴가 유형 엔티티.
 * <p>
 * 휴가의 종류와 정책 정보를 관리한다.
 * </p>
 */
@Entity
@Getter
@Table(
        name = "leave_type",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_leave_type_name", columnNames = "leave_name")
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    /**
     * 휴가 유형을 생성한다.
     *
     * @param req 휴가 유형 생성 요청
     * @return 생성된 휴가 유형 엔티티
     */
    public static LeaveType create(LeaveTypeCreateRequest req) {
        LeaveType leaveType = new LeaveType();

        leaveType.leaveName = req.getLeaveTypeName();
        leaveType.unitDays = req.getUnitDays();
        leaveType.leaveDecrement = req.isLeaveDecrement();
        leaveType.requiresAttachment = req.isRequiresAttachment();
        leaveType.leaveCode = LeaveCode.valueOf(req.getLeaveCode());
        leaveType.leaveDescription = req.getLeaveDescription();

        return leaveType;
    }

    /**
     * 휴가 유형 정보를 수정한다.
     *
     * @param req 휴가 유형 수정 요청
     */
    public void update(LeaveTypeUpdateRequest req) {
        this.leaveName = req.getLeaveTypeName();
        this.unitDays = req.getUnitDays();
        this.leaveDecrement = req.isLeaveDecrement();
        this.requiresAttachment = req.isRequiresAttachment();
        this.leaveCode = LeaveCode.valueOf(req.getLeaveCode());
        this.leaveDescription = req.getLeaveDescription();
    }
}

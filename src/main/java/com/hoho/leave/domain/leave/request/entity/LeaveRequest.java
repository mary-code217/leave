package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestCreateRequest;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "leave_request")
public class LeaveRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 신청자 (필수) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** 휴가 유형 (필수) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /** 상태 (기본 PENDING) */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveRequestStatus status = LeaveRequestStatus.PENDING;

    /** 총 사용량 */
    @Column(name = "quantity_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal quantityDays; 

    /** 시작/종료일 */
    @Column(name = "start_day", nullable = false)
    private LocalDate startDay;

    @Column(name = "end_day")
    private LocalDate endDay;

    /** 시간 단위 사용 시(반차/시간연차 등), 같은 날일 때만 의미 있음 */
    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    /** 첨부파일이 있으면 사용 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_attachment_id", nullable = false)
    private LeaveRequestAttachment attachment = null;

    @Builder
    public LeaveRequest(BigDecimal quantityDays, LocalDate startDay, LocalDate endDay, LocalTime startTime, LocalTime endTime) {
        this.quantityDays = quantityDays;
        this.startDay = startDay;
        this.endDay = endDay;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static LeaveRequest create(LeaveRequestCreateRequest req) {
        return LeaveRequest.builder()
                .quantityDays(req.getQuantityDays())
                .startDay(req.getStartDay())
                .endDay(req.getEndDay())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .build();
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void addLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public void addAttachment(LeaveRequestAttachment attachment) {
        this.attachment = attachment;
    }

    public void updateStatus(LeaveRequestStatus status) {
        this.status = status;
    }
}

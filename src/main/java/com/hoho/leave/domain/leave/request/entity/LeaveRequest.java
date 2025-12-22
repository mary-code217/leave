package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestCreateRequest;
import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 휴가 신청 엔티티.
 * <p>
 * 사용자가 신청한 휴가 정보를 관리한다.
 * </p>
 */
@Entity
@Getter
@Table(name = "leave_request")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
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

    /** 첨부파일(복수) */
    @OneToMany(mappedBy = "leaveRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRequestAttachment> attachments = new ArrayList<>();

    /**
     * 휴가 신청 요청으로부터 엔티티를 생성한다.
     *
     * @param request 휴가 신청 생성 요청
     * @return 생성된 휴가 신청 엔티티
     */
    public static LeaveRequest create(LeaveRequestCreateRequest request) {
        LeaveRequest leaveRequest = new LeaveRequest();

        leaveRequest.quantityDays = request.getQuantityDays();
        leaveRequest.startDay = request.getStartDay();
        leaveRequest.endDay = request.getEndDay();
        leaveRequest.startTime = request.getStartTime();
        leaveRequest.endTime = request.getEndTime();

        return leaveRequest;
    }

    /**
     * 사용자를 설정한다.
     *
     * @param user 사용자
     */
    public void addUser(User user) {
        this.user = user;
    }

    /**
     * 휴가 유형을 설정한다.
     *
     * @param leaveType 휴가 유형
     */
    public void addLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    /**
     * 첨부파일을 추가한다.
     *
     * @param attachment 첨부파일
     */
    public void addAttachment(LeaveRequestAttachment attachment) {
        attachments.add(attachment);
        attachment.addLeaveRequest(this);
    }

    /**
     * 첨부파일을 제거한다.
     *
     * @param attachment 첨부파일
     */
    public void removeAttachment(LeaveRequestAttachment attachment) {
        attachments.remove(attachment);
        attachment.addLeaveRequest(null);
    }

    /**
     * 휴가 신청 상태를 변경한다.
     *
     * @param status 변경할 상태
     */
    public void updateStatus(LeaveRequestStatus status) {
        this.status = status;
    }
}

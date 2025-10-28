package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.domain.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(
        name = "leave_request_approval",
        uniqueConstraints = {
                // 한 신청건 + 같은 단계 중복 방지 (단일 승인자 단계일 때)
                @UniqueConstraint(name = "uq_lra_request_step", columnNames = {"leave_request_id", "step_no"})
        }
)
@NoArgsConstructor
public class LeaveRequestApproval extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;          // 대상 휴가 신청건

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private User approver;            // 지정된 승인자(원 승인자, 위임 전 기준)

    @Column(name = "step_no", nullable = false)
    private Integer stepNo;             // 결재 단계

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;             //진행 상태(결재, 승인, 반려)

    @Column(name = "comment")
    private String comment;             // 사유 메모

    // 실제 승인/반려를 수행한 사용자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acted_by_id")
    private User actedBy;         // 실제 처리자(위임 시 수임자, 없으면 approver)

    @Column(name = "acted_at")
    private LocalDateTime actedAt;        // 실제 처리 시각

    @Builder
    public LeaveRequestApproval(LeaveRequest leaveRequest, User approver, Integer stepNo) {
        this.leaveRequest = leaveRequest;
        this.approver = approver;
        this.stepNo = stepNo;
    }

    public static LeaveRequestApproval create(LeaveRequest leaveRequest, User approver, Integer stepNo) {
        return LeaveRequestApproval.builder()
                .leaveRequest(leaveRequest)
                .approver(approver)
                .stepNo(stepNo)
                .build();
    }

    public void update(ApprovalStatus status, String comment) {
        this.status = status;
        this.comment = comment;
        this.actedBy = null;
        this.actedAt = LocalDateTime.now();
    }
}

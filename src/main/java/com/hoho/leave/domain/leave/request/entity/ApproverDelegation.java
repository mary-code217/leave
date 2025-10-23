package com.hoho.leave.domain.leave.request.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Getter
@Table(
        name = "approver_delegation",
        uniqueConstraints = {
                // 정책 예시) 같은 위임자(delegator)가 같은 '시작일'로 중복 등록 금지
                @UniqueConstraint(name = "uq_ad_delegator_start", columnNames = {"delegator_id", "start_date"})
        }
)
public class ApproverDelegation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 위임자(원 승인자) - 필수 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "delegator_id", nullable = false)
    private User delegator;

    /** 수임자(대신 결재) - 필수 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delegate_id", nullable = false)
    private User delegate;

    /** 시작/종료일 */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate; // null = 무기한

    /** 활성 여부 (기본 true) */
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    /** 메모 */
    @Column(name = "comment", length = 500)
    private String comment;
}

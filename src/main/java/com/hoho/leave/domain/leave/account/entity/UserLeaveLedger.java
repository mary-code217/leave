package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_leave_ledger")
public class UserLeaveLedger extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_leaves_id")
    private UserLeaves userLeaves;

    @Column(name = "effective_at")
    private LocalDateTime effectiveAt;

    @Column(name = "amount")
    private String amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason")
    private ReasonCode reasonCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id")
    private LeaveRequest leaveRequest;

    @Column(name = "note", nullable = false)
    private String note;
}

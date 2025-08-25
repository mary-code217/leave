package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.config.BaseEntity;
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

    @Column(name = "tx_date")
    private LocalDateTime txDate;

    @Column(name = "amount")
    private String amount;

    @Column(name = "reason")
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_request_id")
    private LeaveRequest leaveRequest;

    @Column(name = "note")
    private String note;
}

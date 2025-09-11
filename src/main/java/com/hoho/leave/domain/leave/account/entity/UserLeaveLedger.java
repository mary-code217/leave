package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "user_leave_ledger")
@NoArgsConstructor
public class UserLeaveLedger extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_leaves_id", nullable = false)
    private UserLeaves userLeaves;

    @Column(name = "effective_at", nullable = false)
    private LocalDateTime effectiveAt;

    @Column(name = "amount", nullable = false)
    private String amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReasonCode reasonCode;

    @Column(name = "note")
    private String note;

    @Builder
    public UserLeaveLedger(UserLeaves userLeaves, LocalDateTime effectiveAt, String amount, ReasonCode reasonCode, String note) {
        this.userLeaves = userLeaves;
        this.effectiveAt = effectiveAt;
        this.amount = amount;
        this.reasonCode = reasonCode;
        this.note = note;
    }

    public static UserLeaveLedger create(UserLeaves userLeaves, LocalDateTime effectiveAt,
                                         String amount, ReasonCode reasonCode, String note) {

        return UserLeaveLedger.builder()
                .userLeaves(userLeaves)
                .effectiveAt(effectiveAt)
                .amount(amount)
                .reasonCode(reasonCode)
                .note(note).build();
    }
}

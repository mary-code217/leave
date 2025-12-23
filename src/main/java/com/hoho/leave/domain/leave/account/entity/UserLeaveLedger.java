package com.hoho.leave.domain.leave.account.entity;

import com.hoho.leave.domain.leave.account.service.support.LedgerRecord;
import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 휴가 원장 엔티티.
 * 
 * 사용자의 휴가 증감 내역을 기록한다.
 * 부여, 소멸, 사용, 환급 등 모든 휴가 변동 사항을 추적한다.
 * 
 */
@Entity
@Getter
@Table(name = "user_leave_ledger")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    /**
     * 원장 레코드로부터 휴가 원장 엔티티를 생성한다.
     *
     * @param ledgerRecord 원장 레코드
     * @return 생성된 휴가 원장 엔티티
     */
    public static UserLeaveLedger create(LedgerRecord ledgerRecord) {
        UserLeaveLedger userLeaveLedger = new UserLeaveLedger();

        userLeaveLedger.userLeaves = ledgerRecord.getUserLeaves();
        userLeaveLedger.effectiveAt = ledgerRecord.getEffectiveAt();
        userLeaveLedger.amount = ledgerRecord.getAmount();
        userLeaveLedger.reasonCode = ledgerRecord.getReasonCode();
        userLeaveLedger.note = ledgerRecord.getNote();

        return userLeaveLedger;
    }
}

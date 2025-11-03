package com.hoho.leave.domain.leave.account.service.support;

import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LedgerRecord {
    UserLeaves userLeaves;

    LocalDateTime effectiveAt;

    String amount;

    ReasonCode reasonCode;

    String note;

    public static LedgerRecord of(UserLeaves userLeaves, LocalDateTime effectiveAt,
                                  String amount, ReasonCode reasonCode, String note) {
        LedgerRecord ledgerRecord = new LedgerRecord();

        ledgerRecord.userLeaves = userLeaves;
        ledgerRecord.effectiveAt = effectiveAt;
        ledgerRecord.amount = amount;
        ledgerRecord.reasonCode = reasonCode;
        ledgerRecord.note = note;

        return ledgerRecord;
    }
}

package com.hoho.leave.domain.leave.account.service.support;

import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 휴가 원장 레코드 DTO.
 * 
 * 휴가 원장 생성에 필요한 정보를 담는다.
 * 
 */
@Data
public class LedgerRecord {
    UserLeaves userLeaves;

    LocalDateTime effectiveAt;

    String amount;

    ReasonCode reasonCode;

    String note;

    /**
     * 휴가 원장 레코드를 생성한다.
     *
     * @param userLeaves 사용자 휴가 계정
     * @param effectiveAt 적용 일시
     * @param amount 증감량
     * @param reasonCode 사유 코드
     * @param note 비고
     * @return 원장 레코드
     */
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

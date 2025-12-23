package com.hoho.leave.domain.leave.account.dto.response;

import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 휴가 원장 상세 응답 DTO.
 * 
 * 개별 휴가 원장 항목의 상세 정보를 담는다.
 * 
 */
@Data
public class LeaveLedgerDetailResponse {
    Long id;

    String username;

    String employeeNo;

    LocalDateTime effectiveAt;

    String amount;

    ReasonCode reasonCode;

    String note;

    /**
     * 휴가 원장 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param ledger 휴가 원장 엔티티
     * @return 휴가 원장 상세 응답
     */
    public static LeaveLedgerDetailResponse of(UserLeaveLedger ledger) {
        LeaveLedgerDetailResponse response = new LeaveLedgerDetailResponse();

        response.id = ledger.getId();
        response.username = ledger.getUserLeaves().getUser().getUsername();
        response.employeeNo = ledger.getUserLeaves().getUser().getEmployeeNo();
        response.effectiveAt = ledger.getEffectiveAt();
        response.amount = ledger.getAmount();
        response.reasonCode = ledger.getReasonCode();
        response.note = ledger.getNote();

        return response;
    }
}

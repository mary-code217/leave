package com.hoho.leave.domain.leave.account.dto.response;

import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LeaveLedgerDetailResponse {
    Long id;

    String username;

    String employeeNo;

    LocalDateTime effectiveAt;

    String amount;

    ReasonCode reasonCode;

    String note;

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

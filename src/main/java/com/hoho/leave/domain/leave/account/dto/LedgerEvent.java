package com.hoho.leave.domain.leave.account.dto;

import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LedgerEvent {

    UserLeaves userLeaves;
    LocalDateTime effectiveAt;
    String amount;
    ReasonCode reasonCode;
    String note;

    public static LedgerEvent of(UserLeaves userLeaves, LocalDateTime effectiveAt,
                                 String amount, ReasonCode reasonCode, String note) {
        return LedgerEvent.builder()
                .userLeaves(userLeaves)
                .effectiveAt(effectiveAt)
                .amount(amount)
                .reasonCode(reasonCode)
                .note(note).build();
    }
}

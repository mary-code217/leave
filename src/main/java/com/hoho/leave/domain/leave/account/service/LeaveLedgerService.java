package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.domain.leave.account.dto.LedgerEvent;
import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.repository.UserLeaveLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveLedgerService {

    private final UserLeaveLedgerRepository userLeaveLedgerRepository;

    public UserLeaveLedger createUserLeaveLedger(LedgerEvent ledgerEvent) {
        return userLeaveLedgerRepository.save(UserLeaveLedger.create(
                ledgerEvent.getUserLeaves(),
                ledgerEvent.getEffectiveAt(),
                ledgerEvent.getAmount(),
                ledgerEvent.getReasonCode(),
                ledgerEvent.getNote()));
    }
}

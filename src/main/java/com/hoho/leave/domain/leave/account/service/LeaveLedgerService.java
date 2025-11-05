package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.domain.leave.account.service.support.LedgerRecord;
import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import com.hoho.leave.domain.leave.account.repository.UserLeaveLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveLedgerService {

    private final UserLeaveLedgerRepository userLeaveLedgerRepository;

    public UserLeaveLedger createUserLeaveLedger(LedgerRecord ledgerRecord) {
        return userLeaveLedgerRepository.save(UserLeaveLedger.create(ledgerRecord));
    }
}

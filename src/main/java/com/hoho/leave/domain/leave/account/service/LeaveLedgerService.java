package com.hoho.leave.domain.leave.account.service;

import com.hoho.leave.domain.leave.account.dto.response.LeaveLedgerDetailResponse;
import com.hoho.leave.domain.leave.account.dto.response.LeaveLedgerListResponse;
import com.hoho.leave.domain.leave.account.service.support.LedgerRecord;
import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import com.hoho.leave.domain.leave.account.repository.UserLeaveLedgerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 유저 휴가 원장 서비스
 */
@Service
@RequiredArgsConstructor
public class LeaveLedgerService {

    private final UserLeaveLedgerRepository ledgerRepository;

    public void createUserLeaveLedger(LedgerRecord ledgerRecord) {
        ledgerRepository.save(UserLeaveLedger.create(ledgerRecord));
    }

    @Transactional(readOnly = true)
    public LeaveLedgerListResponse getLeaveLedgers(Integer page, Integer size) {
        Pageable pageable = getPageable(size, page);
        Page<UserLeaveLedger> pageList = ledgerRepository.findAll(pageable);
        List<LeaveLedgerDetailResponse> list = pageList.map(LeaveLedgerDetailResponse::of).toList();

        return LeaveLedgerListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보 생성
     */
    private PageRequest getPageable(Integer size, Integer page) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("id")));
    }
}

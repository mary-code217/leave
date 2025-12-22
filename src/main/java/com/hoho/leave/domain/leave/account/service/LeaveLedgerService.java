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
 * 사용자 휴가 원장 서비스.
 * <p>
 * 휴가 원장의 생성, 조회 기능을 제공한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class LeaveLedgerService {

    private final UserLeaveLedgerRepository ledgerRepository;

    /**
     * 사용자 휴가 원장을 생성한다.
     *
     * @param ledgerRecord 원장 레코드
     */
    public void createUserLeaveLedger(LedgerRecord ledgerRecord) {
        ledgerRepository.save(UserLeaveLedger.create(ledgerRecord));
    }

    /**
     * 휴가 원장 목록을 페이징하여 조회한다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 원장 목록 응답
     */
    @Transactional(readOnly = true)
    public LeaveLedgerListResponse getLeaveLedgers(Integer page, Integer size) {
        Pageable pageable = getPageable(size, page);
        Page<UserLeaveLedger> pageList = ledgerRepository.findAll(pageable);
        List<LeaveLedgerDetailResponse> list = pageList.map(LeaveLedgerDetailResponse::of).toList();

        return LeaveLedgerListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보를 생성한다.
     *
     * @param size 페이지 크기
     * @param page 페이지 번호
     * @return 페이지 요청 정보
     */
    private PageRequest getPageable(Integer size, Integer page) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc("id")));
    }
}

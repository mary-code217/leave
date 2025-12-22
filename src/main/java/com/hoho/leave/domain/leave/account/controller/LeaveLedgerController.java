package com.hoho.leave.domain.leave.account.controller;

import com.hoho.leave.domain.leave.account.dto.response.LeaveLedgerListResponse;
import com.hoho.leave.domain.leave.account.service.LeaveLedgerService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 휴가 원장 컨트롤러.
 * <p>
 * 사용자의 휴가 원장 조회 기능을 제공한다.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/ledger"))
public class LeaveLedgerController {

    private final LeaveLedgerService leaveLedgerService;

    /**
     * 휴가 원장 목록을 조회한다.
     *
     * @param page 페이지 번호 (기본값: 1, 최소값: 1)
     * @param size 페이지 크기 (기본값: 5, 최소값: 1, 최대값: 20)
     * @return 휴가 원장 목록 응답
     */
    @GetMapping("")
    public ResponseEntity<?> getLeaveLedgers(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                             @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {
        LeaveLedgerListResponse response = leaveLedgerService.getLeaveLedgers(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

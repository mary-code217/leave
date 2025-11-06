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

@RequiredArgsConstructor
@RestController
@RequestMapping(("/api/v1/ledger"))
public class LeaveLedgerController {

    private final LeaveLedgerService leaveLedgerService;

    @GetMapping("")
    public ResponseEntity<?> getLeaveLedgers(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                             @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {
        LeaveLedgerListResponse response = leaveLedgerService.getLeaveLedgers(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.facade.ConcurrencyPolicyFacade;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concurrency")
public class LeaveConcurrencyController {

    private final ConcurrencyPolicyFacade service;

    @PostMapping("")
    public ResponseEntity<?> createLeaveConcurrency(
            @RequestBody @Valid LeaveConcurrencyPolicyRequest request) {

        service.createLeaveConcurrencyPolicy(request);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 정책 생성");
    }
}

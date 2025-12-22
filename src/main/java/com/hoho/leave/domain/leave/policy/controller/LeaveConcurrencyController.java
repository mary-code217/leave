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

/**
 * 휴가 동시 제한 정책 컨트롤러.
 * <p>
 * 휴가 동시 제한 정책의 생성 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/concurrency")
public class LeaveConcurrencyController {

    private final ConcurrencyPolicyFacade policyFacade;

    /**
     * 휴가 동시 제한 정책을 생성한다.
     *
     * @param request 정책 생성 요청
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createLeaveConcurrency(
            @RequestBody @Valid LeaveConcurrencyPolicyRequest request) {

        policyFacade.createLeaveConcurrencyPolicy(request);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 정책 생성");
    }
}

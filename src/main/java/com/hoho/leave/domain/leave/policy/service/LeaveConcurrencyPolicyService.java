package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import com.hoho.leave.domain.leave.policy.repository.LeaveConcurrencyPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveConcurrencyPolicyService {

    private final LeaveConcurrencyPolicyRepository policyRepository;

    /** 동시휴가정책 생성시 검증해야할 것들... **/
    public void createLeaveConcurrencyPolicy(LeaveConcurrencyPolicyRequest request) {
        
    }
}

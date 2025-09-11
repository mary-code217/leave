package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.domain.leave.policy.dto.request.CreateConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.repository.LeaveConcurrencyPolicyRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveConcurrencyPolicyService {

    private final LeaveConcurrencyPolicyRepository policyRepository;

    public LeaveConcurrencyPolicy createLeaveConcurrencyPolicy(CreateConcurrencyPolicy event) {
        boolean isErr = policyRepository.existsOverlappingHalfOpen(
                event.getTeam().getId(), event.getLeaveType().getId(),
                event.getEffectiveFrom(), event.getEffectiveTo());
        if(isErr) throw new BusinessException("생성 실패 - 날짜가 겹치는 정책이 존재합니다.");

        return policyRepository.save(LeaveConcurrencyPolicy.create(
                event.getTeam(),
                event.getLeaveType(),
                event.getMaxConcurrent(),
                event.getEffectiveFrom(),
                event.getEffectiveTo()
        ));
    }
}

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

    // 동시휴가정책 생성
    public LeaveConcurrencyPolicy createLeaveConcurrencyPolicy(CreateConcurrencyPolicy dto) {
        boolean isErr = policyRepository.existsOverlappingHalfOpen(
                dto.getTeam().getId(), dto.getLeaveType().getId(),
                dto.getEffectiveFrom(), dto.getEffectiveTo());
        if (isErr) throw new BusinessException("생성 실패 - 날짜가 겹치는 정책이 존재합니다.");

        return policyRepository.save(LeaveConcurrencyPolicy.create(
                dto.getTeam(),
                dto.getLeaveType(),
                dto.getMaxConcurrent(),
                dto.getEffectiveFrom(),
                dto.getEffectiveTo()
        ));
    }

    // 동시휴가정책 가져오기(전체 - 현재 유효한 정책 and 미래에 실행될 정책 - 메인화면 표시용도)
    

    // 동시휴가정책 가져오기(부서 - 휴가기간사이의 정책 - 휴가신청시 확인용도)
}

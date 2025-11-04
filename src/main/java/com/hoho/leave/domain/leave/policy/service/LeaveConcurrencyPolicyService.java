package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.repository.LeaveConcurrencyPolicyRepository;
import com.hoho.leave.domain.leave.policy.service.support.CreateConcurrencyPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeaveConcurrencyPolicyService {

    private final LeaveConcurrencyPolicyRepository policyRepository;

    // 동시휴가정책 생성
    public LeaveConcurrencyPolicy createLeaveConcurrencyPolicy(CreateConcurrencyPolicy request) {
        checkDuplicatePolicy(request);
        return policyRepository.save(LeaveConcurrencyPolicy.create(request));
    }

    // 동시휴가정책 가져오기(전체 - 현재 유효한 정책 and 미래에 실행될 정책 - 메인화면 표시용도)
    

    // 동시휴가정책 가져오기(부서 - 휴가기간사이의 정책 - 휴가신청시 확인용도)

    /** 중복 정책 체크 */
    private void checkDuplicatePolicy(CreateConcurrencyPolicy request) {
        boolean isErr = policyRepository.existsOverlappingHalfOpen(
                request.getTeam().getId(), request.getLeaveType().getId(),
                request.getEffectiveFrom(), request.getEffectiveTo());
        if (isErr) throw new DuplicateException("Duplicate Leave Concurrency Policy exists");
    }
}

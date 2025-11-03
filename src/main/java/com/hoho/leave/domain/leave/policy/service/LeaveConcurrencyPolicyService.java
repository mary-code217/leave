package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.repository.LeaveConcurrencyPolicyRepository;
import com.hoho.leave.domain.leave.policy.service.support.ConcurrencyPolicyParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveConcurrencyPolicyService {

    private final LeaveConcurrencyPolicyRepository policyRepository;

    public LeaveConcurrencyPolicy createConcurrencyPolicy(ConcurrencyPolicyParams params) {
        checkDuplicatePolicy(params);
        return policyRepository.save(LeaveConcurrencyPolicy.create(params));
    }

    // 동시휴가정책 가져오기(전체 - 현재 유효한 정책 and 미래에 실행될 정책 - 메인화면 표시용도)
    

    public boolean checkTeamConcurrencyPolicyRange(Long teamId, LocalDate from, LocalDate to) {
        return policyRepository.existsCoveringRange(teamId, from, to);
    }

    public Integer getMaxConcurrent(Long teamId, LocalDate from, LocalDate to) {
        return policyRepository.findCoveringRange(teamId, from, to).getFirst().getMaxConcurrent();
    }

    /** 중복 정책 체크 */
    private void checkDuplicatePolicy(ConcurrencyPolicyParams params) {
        boolean isErr = policyRepository.existsOverlappingHalfOpen(
                params.getTeam().getId(), params.getLeaveType().getId(),
                params.getEffectiveFrom(), params.getEffectiveTo());
        if (isErr) throw new DuplicateException("Duplicate Leave Concurrency Policy exists");
    }
}

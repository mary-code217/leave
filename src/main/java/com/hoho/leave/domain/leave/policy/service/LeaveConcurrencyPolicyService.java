package com.hoho.leave.domain.leave.policy.service;

import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.repository.LeaveConcurrencyPolicyRepository;
import com.hoho.leave.domain.leave.policy.service.support.ConcurrencyPolicyParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * 휴가 동시 제한 정책 서비스.
 * 
 * 휴가 동시 제한 정책의 생성 및 조회 기능을 제공한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class LeaveConcurrencyPolicyService {

    private final LeaveConcurrencyPolicyRepository policyRepository;

    /**
     * 휴가 동시 제한 정책을 생성한다.
     *
     * @param params 정책 생성 파라미터
     * @return 생성된 정책 엔티티
     */
    public LeaveConcurrencyPolicy createConcurrencyPolicy(ConcurrencyPolicyParams params) {
        checkDuplicatePolicy(params);
        return policyRepository.save(LeaveConcurrencyPolicy.create(params));
    }

    /**
     * 팀에 특정 기간을 포함하는 정책이 존재하는지 확인한다.
     *
     * @param teamId 팀 ID
     * @param from 시작일
     * @param to 종료일
     * @return 존재 여부
     */
    public boolean checkTeamConcurrencyPolicyRange(Long teamId, LocalDate from, LocalDate to) {
        return policyRepository.existsCoveringRange(teamId, from, to);
    }

    /**
     * 특정 기간에 적용되는 최대 동시 휴가 인원수를 조회한다.
     *
     * @param teamId 팀 ID
     * @param from 시작일
     * @param to 종료일
     * @return 최대 동시 휴가 인원수
     */
    public Integer getMaxConcurrent(Long teamId, LocalDate from, LocalDate to) {
        return policyRepository.findCoveringRange(teamId, from, to).getFirst().getMaxConcurrent();
    }

    /**
     * 중복 정책 여부를 확인한다.
     *
     * @param params 정책 생성 파라미터
     */
    private void checkDuplicatePolicy(ConcurrencyPolicyParams params) {
        boolean isErr = policyRepository.existsOverlappingHalfOpen(
                params.getTeam().getId(), params.getLeaveType().getId(),
                params.getEffectiveFrom(), params.getEffectiveTo());
        if (isErr) throw new DuplicateException("Duplicate Leave Concurrency Policy exists");
    }
}

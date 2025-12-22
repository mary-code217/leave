package com.hoho.leave.domain.leave.policy.service.support;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.org.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 휴가 동시 제한 정책 생성 파라미터.
 * <p>
 * 휴가 동시 제한 정책 생성에 필요한 데이터를 담는다.
 * </p>
 */
@Getter
@Builder
public class ConcurrencyPolicyParams {

    private final Team team;

    private final LeaveType leaveType;

    private final Integer maxConcurrent;

    private final LocalDate effectiveFrom;

    private final LocalDate effectiveTo;

    /**
     * 팀, 휴가 유형, 요청 정보로부터 파라미터 객체를 생성한다.
     *
     * @param team 팀 엔티티
     * @param leaveType 휴가 유형 엔티티
     * @param request 정책 생성 요청
     * @return 정책 생성 파라미터
     */
    public static ConcurrencyPolicyParams of(Team team, LeaveType leaveType,
                                             LeaveConcurrencyPolicyRequest request) {

        return ConcurrencyPolicyParams.builder()
                .team(team)
                .leaveType(leaveType)
                .maxConcurrent(request.getMaxConcurrent())
                .effectiveFrom(request.getEffectiveFrom())
                .effectiveTo(request.getEffectiveTo())
                .build();
    }
}

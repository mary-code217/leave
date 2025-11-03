package com.hoho.leave.domain.leave.policy.service.support;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.org.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class ConcurrencyPolicyParams {

    private final Team team;

    private final LeaveType leaveType;

    private final Integer maxConcurrent;

    private final LocalDate effectiveFrom;

    private final LocalDate effectiveTo;

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

package com.hoho.leave.domain.leave.policy.service.support;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.org.entity.Team;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CreateConcurrencyPolicy {

    private final Team team;

    private final LeaveType leaveType;

    private final Integer maxConcurrent;

    private final LocalDate effectiveFrom;

    private final LocalDate effectiveTo;

    public static CreateConcurrencyPolicy of(Team team, LeaveType leaveType, Integer maxConcurrent,
                                             LocalDate effectiveFrom, LocalDate effectiveTo) {

        return CreateConcurrencyPolicy.builder()
                .team(team)
                .leaveType(leaveType)
                .maxConcurrent(maxConcurrent)
                .effectiveFrom(effectiveFrom)
                .effectiveTo(effectiveTo)
                .build();
    }
}

package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.leave.policy.dto.request.CreateConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.service.LeaveConcurrencyPolicyService;
import com.hoho.leave.domain.leave.policy.service.LeaveTypeService;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConcurrencyPolicyApplicationService {

    private final TeamService teamService;
    private final LeaveTypeService leaveTypeService;
    private final LeaveConcurrencyPolicyService leaveConcurrencyPolicyService;

    @Transactional
    public void createLeaveConcurrencyPolicy(LeaveConcurrencyPolicyRequest req) {
        Team team = teamService.getTeamEntity(req.getTeamId());

        LeaveType leaveType = leaveTypeService.getLeaveTypeEntity(req.getLeaveTypeId());

        leaveConcurrencyPolicyService.createLeaveConcurrencyPolicy(new CreateConcurrencyPolicy(team, leaveType, req.getMaxConcurrent(), req.getEffectiveFrom(), req.getEffectiveTo()));
    }
}

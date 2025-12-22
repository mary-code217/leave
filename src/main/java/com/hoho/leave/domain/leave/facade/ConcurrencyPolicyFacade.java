package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.leave.policy.service.support.ConcurrencyPolicyParams;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveConcurrencyPolicyRequest;
import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.leave.policy.service.LeaveConcurrencyPolicyService;
import com.hoho.leave.domain.leave.policy.service.LeaveTypeService;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 휴가 동시 제한 정책 파사드.
 * <p>
 * 휴가 동시 제한 정책 생성 시 여러 도메인 서비스를 조율한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ConcurrencyPolicyFacade {

    private final TeamService teamService;
    private final LeaveTypeService leaveTypeService;
    private final LeaveConcurrencyPolicyService leaveConcurrencyPolicyService;
    private final AuditLogService auditLogService;

    /**
     * 부서별 휴가 동시 제한 정책을 생성한다.
     *
     * @param request 휴가 동시 제한 정책 생성 요청
     */
    @Transactional
    public void createLeaveConcurrencyPolicy(LeaveConcurrencyPolicyRequest request) {
        Team team = teamService.getTeamEntity(request.getTeamId());

        LeaveType leaveType = leaveTypeService.getLeaveTypeEntity(request.getLeaveTypeId());

        LeaveConcurrencyPolicy savePolicy =
                leaveConcurrencyPolicyService.createConcurrencyPolicy(
                        ConcurrencyPolicyParams.of(team, leaveType, request)
                );

        auditLogService.createLog(
                Action.LEAVE_POLICY_CONCURRENCY_CREATE,
                null,
                AuditObjectType.LEAVE_POLICY,
                savePolicy.getId(),
                "관리자에 의한 " + team.getTeamName() + "의 휴가 동시 제한(" + savePolicy.getMaxConcurrent() + "명) 생성"
        );
    }
}

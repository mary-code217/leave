package com.hoho.leave.domain.user.facade;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.leave.account.dto.LedgerEvent;
import com.hoho.leave.domain.leave.account.entity.ReasonCode;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import com.hoho.leave.domain.leave.account.service.LeaveAccountService;
import com.hoho.leave.domain.leave.account.service.LeaveLedgerService;
import com.hoho.leave.domain.user.dto.request.UserJoinRequest;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserApplicationService {

    private final UserService userService;
    private final LeaveAccountService leaveAccountService;
    private final LeaveLedgerService leaveLedgerService;
    private final AuditLogService auditLogService;

    @Transactional
    public void createUser(UserJoinRequest userJoinRequest) {

        User saveUser = userService.createUser(userJoinRequest);

        UserLeaves leaves = leaveAccountService.firstCreateUserLeaves(saveUser, userJoinRequest.getBalanceDays());

        leaveLedgerService.createUserLeaveLedger(LedgerEvent.of(
                leaves,
                LocalDateTime.now(),
                "+" + userJoinRequest.getBalanceDays(),
                ReasonCode.MIGRATION_OPENING_BALANCE,
                "유저생성에 의한 초기 휴가설정"
        ));

        auditLogService.createLog(
                Action.USER_JOIN,
                null,
                AuditObjectType.USER,
                saveUser.getId(),
                "관리자에 의한 유저 " + saveUser.getUsername() + "(" + saveUser.getEmployeeNo() + ") 생성"
        );

        auditLogService.createLog(
                Action.LEAVE_ADJUSTMENT_POSTED,
                null,
                AuditObjectType.USER_LEAVES,
                leaves.getId(),
                "관리자에 의한 유저 " + saveUser.getUsername() + "(" + saveUser.getEmployeeNo() +
                        ")의 초기 휴가(" + leaves.getBalanceDays() + "일)부여"
        );
    }
}

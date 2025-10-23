package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.leave.account.service.LeaveAccountService;
import com.hoho.leave.domain.leave.account.service.LeaveLedgerService;
import com.hoho.leave.domain.leave.policy.service.LeaveConcurrencyPolicyService;
import com.hoho.leave.domain.leave.request.service.LeaveRequestService;
import com.hoho.leave.domain.notification.service.NotificationService;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveRequestApplicationService {

    private final UserService userService;

    private final LeaveRequestService leaveRequestService;
    private final LeaveAccountService leaveAccountService;
    private final LeaveLedgerService leaveLedgerService;
    private final LeaveConcurrencyPolicyService leavePolicyService;

    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Transactional
    public void userLeaveRequest() {
        // 신청유저 찾아오기 -> 유저 엔티티를 반환하는 메서드 필요(O)

        // 신청유저 휴가계정 -> 휴가계정 엔티티를 반환하는 메서드 필요(O)

        // 동시정책 -> 휴가신청기간에 동시정책 확인, 없으면 true, 있으면 인원확인 후 true/false 반환하는 메서드 필요

        // 휴가 신청서 분석 -> 휴가타입(연차,기타휴가), 차감여부, 증빙여부 파악

        // 공휴일 체크 -> 공휴일서비스에 휴가기간이 공휴일 며칠인지 확인하는 메서드 필요

        // 결재자들 찾아오기 -> 결재자들 찾아오는 메서드 필요
        // 결재선 등록(O)

        // 휴가 원장 등록(O)
        // 휴가 차감

        // 알림 발송 -> 휴가신청자/결재자들(O)
        // 로그 등록(O)
    }
}

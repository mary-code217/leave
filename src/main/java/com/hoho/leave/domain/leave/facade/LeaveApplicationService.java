package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.leave.account.service.LeaveAccountService;
import com.hoho.leave.domain.leave.account.service.LeaveLedgerService;
import com.hoho.leave.domain.leave.request.service.LeaveRequestService;
import com.hoho.leave.domain.notification.service.NotificationService;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveAccountService leaveAccountService;
    private final LeaveLedgerService leaveLedgerService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;
    private final LeaveRequestService leaveRequestService;
    private final UserService userService;

    @Transactional
    public void userLeaveRequest() {

        // 유저 찾아오기(O)
        // 결재자들 찾아오기(O)
        // 휴가계정 찾아오기(O)
        // 휴가 신청서 분석 -> 휴가타입(연차,기타휴가), 차감여부, 증빙여부 파악 (O)
        // 차감휴가의 경우
        // 휴가계정(O)
        // 동시정책
        // 공휴일 체크
        // 결재선 등록(O)
        // 휴가 원장 등록(O)
        // 알림 발송 -> 휴가신청자/결재자들(O)
        // 로그 등록(O)

    }
}

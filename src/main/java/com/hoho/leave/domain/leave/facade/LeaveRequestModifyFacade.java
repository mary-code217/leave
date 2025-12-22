package com.hoho.leave.domain.leave.facade;

import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.leave.account.service.LeaveAccountService;
import com.hoho.leave.domain.leave.account.service.LeaveLedgerService;
import com.hoho.leave.domain.leave.policy.service.LeaveConcurrencyPolicyService;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.service.AttachmentService;
import com.hoho.leave.domain.leave.request.service.LeaveRequestService;
import com.hoho.leave.domain.notification.service.NotificationService;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 휴가 신청 변경 파사드.
 * <p>
 * 휴가 신청의 생성, 삭제 시 여러 도메인 서비스를 조율한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class LeaveRequestModifyFacade {

    private final UserService userService;

    private final LeaveRequestService leaveRequestService;
    private final AttachmentService attachmentService;
    private final LeaveAccountService leaveAccountService;
    private final LeaveLedgerService leaveLedgerService;
    private final LeaveConcurrencyPolicyService leavePolicyService;

    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    /**
     * 휴가 신청을 생성한다.
     * (미구현)
     */
    @Transactional
    public void createLeaveRequest() {
        // 신청유저 찾아오기 -> 유저 엔티티를 반환하는 메서드 필요(O)

        // 신청유저 휴가계정 -> 휴가계정 엔티티를 반환하는 메서드 필요(O)
        
        // 휴가계정 유효성 검증(잔여일수, 휴가단위) -> 잔여일수 확인 메서드 필요

        // 동시정책 -> 휴가신청기간에 동시정책 확인, 없으면 false, 있으면 true
        // true 반환시 A. 정책 Max 인원 조회 B. 정책 사이 휴가 신청된 인원 조회
        // A, B 비교

        // 공휴일 체크 -> 휴가기간에 공휴일이 포함되어있는지 확인하는 메서드 필요

        // 휴가 신청서 작성 -> 휴가타입(연차,기타휴가), 차감, 증빙파일

        // 결재자들 찾아오기 -> 결재자들 찾아오는 메서드 필요
        // 결재선 등록(O)

        // 휴가 원장 등록(O)
        // 휴가 차감(0)

        // 알림 발송 -> 휴가신청자/결재자들(O)
        // 로그 등록(O)
    }

    /**
     * 휴가 신청을 삭제한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     */
    @Transactional
    public void deleteLeaveRequest(Long leaveRequestId) {
        LeaveRequest leaveRequest = leaveRequestService.getRequestEntity(leaveRequestId);

        attachmentService.deleteAttachments(leaveRequestId);

        leaveRequestService.deleteLeaveRequest(leaveRequest);
    }
}

package com.hoho.leave.domain.audit.entity;

/**
 * 감사 로그에 기록되는 행위 유형을 정의하는 열거형.
 * 
 * 사용자, 조직, 휴가, 알림 등 시스템 내 모든 주요 행위를 분류한다.
 * 
 */
public enum Action {
    // USER (회원)
    USER_JOIN,                          // 회원가입 성공
    USER_UPDATE_PROFILE,                // 프로필/연락처 변경
    USER_UPDATE_PASSWORD,               // 비밀번호 변경
    USER_STATUS_CHANGE,                 // 재직/휴직/퇴사 등 상태 변경
    USER_ROLE_CHANGE,                   // 권한(ROLE) 변경
    USER_DELETE,                        // (권고: 실제로는 비활성화) 물리 삭제 시

    // ORG (조직 마스터)
    ORG_GRADE_CREATE,
    ORG_GRADE_UPDATE,
    ORG_GRADE_DELETE,                   // 권고: 비활성화로 대체
    ORG_POSITION_CREATE,
    ORG_POSITION_UPDATE,
    ORG_POSITION_DELETE,                // 권고: 비활성화로 대체
    ORG_TEAM_CREATE,
    ORG_TEAM_UPDATE,
    ORG_TEAM_MOVE,                      // 부모 변경(트리 이동)
    ORG_TEAM_DELETE,                    // 권고: 비활성화로 대체
    ORG_MEMBERSHIP_ASSIGN,              // 사용자 소속 배정
    ORG_MEMBERSHIP_TRANSFER,            // 소속 이동
    ORG_MEMBERSHIP_END,                 // 소속 종료(이력 남김)

    // LEAVE.POLICY (정책)
    LEAVE_POLICY_TYPE_CREATE,           // 휴가 유형 생성
    LEAVE_POLICY_TYPE_UPDATE,           // 휴가 유형 수정
    LEAVE_POLICY_TYPE_DEPRECATE,        // 휴가 유형 종료/사용중지
    LEAVE_POLICY_CONCURRENCY_CREATE,    // 동시휴가 정책 생성(팀별)
    LEAVE_POLICY_CONCURRENCY_UPDATE,
    LEAVE_POLICY_CONCURRENCY_DEPRECATE,
    LEAVE_POLICY_DEFAULT_APPROVER_SET,  // 기본 결재자 설정
    LEAVE_POLICY_DEFAULT_APPROVER_UPDATE,
    LEAVE_POLICY_DEFAULT_APPROVER_REMOVE,

    // LEAVE.REQUEST (신청 워크플로우/첨부/결재)
    LEAVE_APPLY,                        // 신청 접수(PENDING)
    LEAVE_REVISE,                       // 신청서 수정(재신청 개념)
    LEAVE_CANCEL,                       // 신청 취소
    LEAVE_APPROVE,                      // 승인(단계 승인 포함)
    LEAVE_REJECT,                       // 반려
    LEAVE_ATTACHMENT_ADD,               // 첨부 추가
    LEAVE_ATTACHMENT_REMOVE,            // 첨부 삭제
    LEAVE_APPROVAL_STEP_ASSIGN,         // 결재 단계 배정/재배정
    LEAVE_APPROVAL_STEP_ADVANCE,        // 단계 진행
    LEAVE_APPROVAL_STEP_ROLLBACK,       // 단계 롤백(예외적)
    LEAVE_DELEGATION_CREATE,            // 위임 결재자 등록(시효 포함)
    LEAVE_DELEGATION_UPDATE,
    LEAVE_DELEGATION_REVOKE,

    // LEAVE.ACCOUNT / LEDGER (잔액·원장)
    LEAVE_ACCRUAL_POSTED,               // 부여(+)
    LEAVE_HOLD_POSTED,                  // 신청 시 예약(HOLD)
    LEAVE_DEDUCT_POSTED,                // 승인 시 확정 차감
    LEAVE_RELEASE_POSTED,               // 반려/취소 시 홀드 해제
    LEAVE_REVERSAL_POSTED,              // 승인 후 취소 등으로 차감 반전
    LEAVE_ADJUSTMENT_POSTED,            // 관리자 조정(+/-)

    // LEAVE.HANDOVER (인수인계)
    HANDOVER_CREATE,
    HANDOVER_UPDATE,
    HANDOVER_RECEIVER_ASSIGN,
    HANDOVER_RECEIVER_REMOVE,
    HANDOVER_COMPLETE,
    HANDOVER_CANCEL,

    // AUDIT (자체 관리/보정)
    AUDIT_RETENTION_POLICY_CHANGED,     // 보존정책/아카이빙 변경
    AUDIT_LOG_PURGED,                   // 정책에 따른 파지/정리(메타만 기록)

    // NOTIFICATION (알림)
    NOTIFICATION_ENQUEUED,              // 발송 대기 큐 적재
    NOTIFICATION_SENT,                  // 성공 발송
    NOTIFICATION_FAIL,                  // 발송 실패
    NOTIFICATION_READ,                  // 사용자 열람
    NOTIFICATION_DELETED                // 알림 삭제
}

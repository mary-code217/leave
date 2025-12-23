package com.hoho.leave.domain.notification.entity;

/**
 * 알림 유형 열거형.
 * 
 * 시스템에서 발생할 수 있는 알림의 유형을 정의한다.
 * 
 */
public enum NotificationType {
    /** 휴가 승인 요청 알림 */
    LEAVE_APPROVAL_REQUESTED,

    /** 휴가 상태 변경 알림 */
    LEAVE_STATUS_CHANGED,

    /** 인수인계 배정 알림 */
    HANDOVER_ASSIGNED,

    /** 연차 잔여일수 조정 알림 */
    LEAVE_BALANCE_ADJUSTED
}

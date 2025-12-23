package com.hoho.leave.domain.leave.request.entity;

/**
 * 휴가 신청 상태.
 * 
 * 휴가 신청의 진행 상태를 나타낸다.
 * 
 */
public enum LeaveRequestStatus {
    /** 대기 중 */
    PENDING,
    /** 승인됨 */
    APPROVED,
    /** 반려됨 */
    REJECTED,
    /** 취소됨 */
    CANCELED,
}

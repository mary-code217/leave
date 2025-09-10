package com.hoho.leave.domain.leave.account.entity;

public enum ReasonCode {
    /** 부여(+) **/
    GRANT_REGULAR,            // 연간 정기부여
    GRANT_MONTHLY,            // 월별 부여
    GRANT_COMPENSATORY,       // 대체휴무/보상휴가
    GRANT_BONUS,              // 포상 부여

    /** 소멸 **/
    EXPIRE,                   // 소멸

    /** 신청/결재 **/
    REQUEST_APPLY_DEDUCT,     // 신청 즉시 차감
    REQUEST_REJECTED_REFUND,  // 반려 환급
    REQUEST_CANCELED_REFUND,  // 취소 환급
    REQUEST_REDUCED_REFUND,   // 부분 환급

    /** 조정 **/
    ADJUST_ADMIN,
    MIGRATION_OPENING_BALANCE,// 초기 잔액 세팅
    
}

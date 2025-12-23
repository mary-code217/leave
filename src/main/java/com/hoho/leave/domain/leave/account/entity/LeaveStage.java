package com.hoho.leave.domain.leave.account.entity;

/**
 * 휴가 단계 열거형.
 * 
 * 사용자의 휴가 부여 단계를 나타낸다.
 * 월차 부여 단계, 연차 부여 단계, 해당 없음으로 구분한다.
 * 
 */
public enum LeaveStage {
    MONTHLY,
    ANNUAL,
    NONE
}

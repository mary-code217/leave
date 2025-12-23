package com.hoho.leave.domain.user.entity;

/**
 * 사용자 권한 열거형.
 * 
 * 시스템 내에서 사용자가 가질 수 있는 권한을 정의한다.
 * 
 */
public enum UserRole {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_APPROVER,
}

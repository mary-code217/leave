package com.hoho.leave.domain.audit.service;

/**
 * 감사 로그 대상 객체 유형 상수 클래스.
 * <p>
 * 감사 로그에서 사용하는 객체 유형 식별자를 중앙 집중화하여 관리한다.
 * </p>
 */
public final class AuditObjectType {

    private AuditObjectType() {}

    /** 사용자 */
    public static final String USER = "USER";
    /** 휴가 신청 */
    public static final String LEAVE_REQUEST = "LEAVE_REQUEST";
    /** 사용자 휴가 잔액 */
    public static final String USER_LEAVES = "USER_LEAVES";
    /** 직급 */
    public static final String GRADE = "GRADE";
    /** 직책 */
    public static final String POSITION = "POSITION";
    /** 팀 */
    public static final String TEAM = "TEAM";
    /** 휴가 유형 */
    public static final String LEAVE_TYPE = "LEAVE_TYPE";
    /** 휴가 정책 (기본결재자/동시휴가 등) */
    public static final String LEAVE_POLICY = "LEAVE_POLICY";
    /** 기본 승인자 */
    public static final String DEFAULT_APPROVER = "DEFAULT_APPROVER";
    /** 인수인계 */
    public static final String HANDOVER = "HANDOVER";
}

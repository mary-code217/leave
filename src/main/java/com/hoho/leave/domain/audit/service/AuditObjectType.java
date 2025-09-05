package com.hoho.leave.domain.audit.service;

public final class AuditObjectType {
    private AuditObjectType() {}
    public static final String USER = "USER";
    public static final String LEAVE_REQUEST = "LEAVE_REQUEST";
    public static final String USER_LEAVES = "USER_LEAVES";
    public static final String GRADE = "GRADE";
    public static final String POSITION = "POSITION";
    public static final String TEAM = "TEAM";
    public static final String LEAVE_TYPE = "LEAVE_TYPE";
    public static final String LEAVE_POLICY = "LEAVE_POLICY";              // 기본결재자/동시휴가 등 포괄
    public static final String DEFAULT_APPROVER = "DEFAULT_APPROVER";
    public static final String HANDOVER = "HANDOVER";
}

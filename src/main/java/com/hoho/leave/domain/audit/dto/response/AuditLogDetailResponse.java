package com.hoho.leave.domain.audit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditLogDetailResponse {
    Long id;

    Action action; // 행위코드

    Long userId;

    String username; // 행위자

    String employeeNo; // 행위자 사번

    Long objectId;

    String objectType;

    String summary;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    public static AuditLogDetailResponse of(AuditLog auditLog, User user) {
        AuditLogDetailResponse response = new AuditLogDetailResponse();

        response.id = auditLog.getId();
        response.action = auditLog.getAction();
        response.userId = user != null ? user.getId() : null;
        response.username = user != null ? user.getUsername() : "관리자";
        response.employeeNo = user != null ? user.getEmployeeNo() : null;
        response.objectId = auditLog.getObjectId();
        response.objectType = auditLog.getObjectType();
        response.summary = auditLog.getSummary();
        response.occurredAt = auditLog.getCreatedAt();

        return response;
    }
}

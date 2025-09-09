package com.hoho.leave.domain.audit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDetailResponse {
    Long id;
    Action action; // 행위코드

    Long userId;
    String username; // 행위자
    String employeeNo; // 행위자 사번

    Long objectId;
    String objectType;

    String summary; // 결과 한줄
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt; // 행위시간

    public static AuditLogDetailResponse from(AuditLog auditLog, User user) {
        return new AuditLogDetailResponse(
                auditLog.getId(),
                auditLog.getAction(),
                user != null ? user.getId() : null,
                user != null ? user.getUsername() : "관리자",
                user != null ? user.getEmployeeNo() : null,
                auditLog.getObjectId(),
                auditLog.getObjectType(),
                auditLog.getSummary(),
                auditLog.getCreatedAt()
        );
    }
}

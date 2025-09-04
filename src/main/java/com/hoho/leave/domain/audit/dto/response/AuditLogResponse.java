package com.hoho.leave.domain.audit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.audit.entity.Result;
import com.hoho.leave.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogResponse {
    Long id;
    Action action; // 행위코드
    Result result; // 결과코드

    Long userId;
    String username; // 행위자
    String employeeNo; // 행위자 사번

    Long objectId;
    String objectType;

    String reasonCode;

    String summary; // 결과 한줄
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt; // 행위시간

    public static AuditLogResponse from(AuditLog auditLog, User user) {
        return new AuditLogResponse(
                auditLog.getId(),
                auditLog.getAction(),
                auditLog.getResult(),
                user.getId(),
                user.getUsername(),
                user.getEmployeeNo(),
                auditLog.getObjectId(),
                auditLog.getObjectType(),
                auditLog.getReasonCode() == null ? "-" : auditLog.getReasonCode(),
                auditLog.getSummary(),
                auditLog.getCreatedAt()
        );
    }
}

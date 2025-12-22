package com.hoho.leave.domain.audit.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 감사 로그 상세 정보 응답 DTO.
 */
@Data
public class AuditLogDetailResponse {

    /** 로그 ID */
    Long id;

    /** 행위 코드 */
    Action action;

    /** 행위자 ID */
    Long userId;

    /** 행위자 이름 */
    String username;

    /** 행위자 사번 */
    String employeeNo;

    /** 대상 객체 ID */
    Long objectId;

    /** 대상 객체 유형 */
    String objectType;

    /** 요약 설명 */
    String summary;

    /** 발생 일시 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    /**
     * AuditLog 엔티티와 User 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param auditLog 감사 로그 엔티티
     * @param user     행위자 정보 (null 가능)
     * @return 감사 로그 상세 응답
     */
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

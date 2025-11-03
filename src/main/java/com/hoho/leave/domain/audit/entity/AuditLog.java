package com.hoho.leave.domain.audit.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "audit_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 무엇을
    @Enumerated(EnumType.STRING)
    private Action action;

    // 누가
    @Column(name = "actor_id")
    private Long actorId;

    // 어떤 대상
    @Column(name = "object_type")
    private String objectType;

    @Column(name = "object_id")
    private Long objectId;

    // 요약(사람용 한 줄)
    @Column(name = "summary")
    private String summary;

    public static AuditLog createLog(Action action, Long actorId,
                                     String objectType, Long objectId,
                                     String summary) {
        AuditLog auditLog = new AuditLog();

        auditLog.action = action;
        auditLog.actorId = actorId;
        auditLog.objectType = objectType;
        auditLog.objectId = objectId;
        auditLog.summary = summary;

        return auditLog;
    }
}


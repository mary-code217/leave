package com.hoho.leave.domain.audit.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 시스템 감사 로그를 저장하는 엔티티.
 * 
 * 누가, 무엇을, 어떤 대상에게 행했는지를 기록하여
 * 시스템의 모든 중요 행위를 추적할 수 있게 한다.
 * 
 */
@Entity
@Getter
@Table(name = "audit_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuditLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 수행된 행위 유형 */
    @Enumerated(EnumType.STRING)
    private Action action;

    /** 행위를 수행한 사용자 ID */
    @Column(name = "actor_id")
    private Long actorId;

    /** 행위 대상 객체 유형 */
    @Column(name = "object_type")
    private String objectType;

    /** 행위 대상 객체 ID */
    @Column(name = "object_id")
    private Long objectId;

    /** 사람이 읽을 수 있는 요약 설명 */
    @Column(name = "summary")
    private String summary;

    /**
     * 감사 로그를 생성한다.
     *
     * @param action     행위 유형
     * @param actorId    행위자 ID
     * @param objectType 대상 객체 유형
     * @param objectId   대상 객체 ID
     * @param summary    요약 설명
     * @return 생성된 AuditLog 인스턴스
     */
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


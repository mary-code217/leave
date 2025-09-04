package com.hoho.leave.domain.audit.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "audit_log")
@NoArgsConstructor
public class AuditLog extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 무엇을
    @Enumerated(EnumType.STRING)
    private Action action;

    // 결과
    @Enumerated(EnumType.STRING)
    private Result result;

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

    // 실패 사유 코드(성공이면 NULL)
    @Column(name = "reason_code")
    private String reasonCode;

    public AuditLog(Action action, Result result, Long actorId,
                    String objectType, Long objectId,
                    String summary, String reasonCode) {
        this.action = action;
        this.result = result;
        this.actorId = actorId;
        this.objectType = objectType;
        this.objectId = objectId;
        this.summary = summary;
        this.reasonCode = reasonCode;
    }

    public static AuditLog createLog() {
        return new AuditLog();
    }
}


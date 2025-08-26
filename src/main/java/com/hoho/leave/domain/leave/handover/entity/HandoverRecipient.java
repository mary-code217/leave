package com.hoho.leave.domain.leave.handover.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(
        name = "handover_recipient",
        uniqueConstraints = {
                // 같은 인수인계에 동일 수신자 중복 금지
                @UniqueConstraint(name = "uq_hnr_handover_recipient", columnNames = {"handover_id", "recipient_id"})
        }
)
public class HandoverRecipient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "handover_id", nullable = false)
    private HandoverNote handoverNote;   // FK 필수

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;        // FK 필수
}


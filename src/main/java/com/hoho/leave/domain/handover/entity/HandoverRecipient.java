package com.hoho.leave.domain.handover.entity;

import com.hoho.leave.domain.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(
        name = "handover_recipient",
        uniqueConstraints = {
                // 같은 인수인계에 동일 수신자 중복 금지
                @UniqueConstraint(name = "uq_hnr_handover_recipient", columnNames = {"handover_id", "recipient_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HandoverRecipient extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handover_id", nullable = false)
    private HandoverNote handoverNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    public static HandoverRecipient create(HandoverNote handoverNote, User recipient) {
        HandoverRecipient handoverRecipient = new HandoverRecipient();

        handoverRecipient.handoverNote = handoverNote;
        handoverRecipient.recipient = recipient;

        return handoverRecipient;
    }
}


package com.hoho.leave.domain.handover.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인수인계 수신자 엔티티.
 * 
 * 인수인계 노트와 수신자 간의 다대다 관계를 나타낸다.
 * 동일 인수인계에 같은 수신자가 중복 등록되지 않도록 유니크 제약조건이 설정되어 있다.
 * 
 */
@Entity
@Getter
@Table(
        name = "handover_recipient",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_hnr_handover_recipient", columnNames = {"handover_id", "recipient_id"})
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HandoverRecipient extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 인수인계 노트 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handover_id", nullable = false)
    private HandoverNote handoverNote;

    /** 수신자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    /**
     * 인수인계 수신자를 생성한다.
     *
     * @param handoverNote 인수인계 노트
     * @param recipient    수신자
     * @return 생성된 인수인계 수신자
     */
    public static HandoverRecipient create(HandoverNote handoverNote, User recipient) {
        HandoverRecipient handoverRecipient = new HandoverRecipient();

        handoverRecipient.handoverNote = handoverNote;
        handoverRecipient.recipient = recipient;

        return handoverRecipient;
    }
}


package com.hoho.leave.domain.notification.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 알림 엔티티.
 * 
 * 사용자에게 발송되는 알림 정보를 저장하고 관리한다.
 * 휴가 승인 요청, 상태 변경, 인수인계 배정, 연차 잔여일수 조정 등의 알림 유형을 지원한다.
 * 
 */
@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    /**
     * 새로운 알림을 생성한다.
     *
     * @param recipient 알림을 받을 사용자
     * @param type 알림 유형
     * @param content 알림 내용
     * @return 생성된 알림 엔티티
     */
    public static Notification create(User recipient, NotificationType type, String content) {
        Notification notification = new Notification();

        notification.recipient = recipient;
        notification.type = type;
        notification.content = content;

        return notification;
    }

    /**
     * 알림을 읽음 처리한다.
     * 
     * 현재 시각으로 읽은 시각을 설정한다.
     * 
     */
    public void updateReadAt() {
        this.readAt = LocalDateTime.now();
    }
}
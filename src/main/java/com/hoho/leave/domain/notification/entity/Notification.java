package com.hoho.leave.domain.notification.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    public static Notification create(User recipient, NotificationType type, String content) {
        Notification notification = new Notification();

        notification.recipient = recipient;
        notification.type = type;
        notification.content = content;

        return notification;
    }

    public void updateReadAt() {
        this.readAt = LocalDateTime.now();
    }
}
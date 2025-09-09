package com.hoho.leave.domain.notification.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "notification")
@NoArgsConstructor
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

    @Builder
    public Notification(User recipient, NotificationType type, String content, LocalDateTime readAt) {
        this.recipient = recipient;
        this.type = type;
        this.content = content;
        this.readAt = readAt;
    }

    public static Notification create(User recipient, NotificationType type, String content) {
        return Notification.builder()
                .recipient(recipient)
                .type(type)
                .content(content)
                .build();
    }

    public void updateReadAt() {
        this.readAt = LocalDateTime.now();
    }
}
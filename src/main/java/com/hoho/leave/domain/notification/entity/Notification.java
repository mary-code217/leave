package com.hoho.leave.domain.notification.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "notification")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "read_at")
    private LocalDateTime readAt;
}
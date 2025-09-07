package com.hoho.leave.domain.leave.handover.entity;

import com.hoho.leave.config.jpa.BaseEntity;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "handover_note")
@NoArgsConstructor
public class HandoverNote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    public HandoverNote(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    public static HandoverNote create(User author, String title, String content) {
        return new HandoverNote(author, title, content);
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}


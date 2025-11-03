package com.hoho.leave.domain.handover.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "handover_note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static HandoverNote create(User author, String title, String content) {
        HandoverNote handoverNote = new HandoverNote();

        handoverNote.author = author;
        handoverNote.title = title;
        handoverNote.content = content;

        return handoverNote;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}


package com.hoho.leave.domain.handover.entity;

import com.hoho.leave.domain.shared.BaseEntity;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 인수인계 노트 엔티티.
 * 
 * 휴가 시 업무 인수인계 내용을 저장한다.
 * 
 */
@Entity
@Getter
@Table(name = "handover_note")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HandoverNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 작성자 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /** 제목 */
    @Column(name = "title", nullable = false)
    private String title;

    /** 내용 */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 인수인계 노트를 생성한다.
     *
     * @param author  작성자
     * @param title   제목
     * @param content 내용
     * @return 생성된 인수인계 노트
     */
    public static HandoverNote create(User author, String title, String content) {
        HandoverNote handoverNote = new HandoverNote();

        handoverNote.author = author;
        handoverNote.title = title;
        handoverNote.content = content;

        return handoverNote;
    }

    /**
     * 인수인계 노트를 수정한다.
     *
     * @param title   새 제목
     * @param content 새 내용
     */
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}


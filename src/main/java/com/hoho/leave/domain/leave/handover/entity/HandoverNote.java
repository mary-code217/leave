package com.hoho.leave.domain.leave.handover.entity;

import com.hoho.leave.config.BaseEntity;
import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "handover_note")
public class HandoverNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 어떤 휴가 신청건의 인수인계인지 (필수) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;

    /** 작성자 (필수) */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    /** 제목 (필수) */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /** 내용 (필수, 길어질 수 있으므로 LOB) */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", nullable = false)
    private String content;
}


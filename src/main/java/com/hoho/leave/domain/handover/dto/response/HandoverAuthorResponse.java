package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 발신 인수인계 응답 DTO.
 */
@Data
public class HandoverAuthorResponse {

    /** 인수인계 노트 ID */
    Long handoverNoteId;

    /** 작성자 이름 */
    String authorName;

    /** 수신자 이름 목록 */
    List<String> recipientName;

    /** 제목 */
    String title;

    /** 내용 */
    String content;

    /** 작성 일시 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    /**
     * 인수인계 노트와 수신자 이름 목록으로 응답 DTO를 생성한다.
     *
     * @param note          인수인계 노트 엔티티
     * @param recipientName 수신자 이름 목록
     * @return 발신 인수인계 응답
     */
    public static HandoverAuthorResponse of(HandoverNote note, List<String> recipientName) {
        HandoverAuthorResponse response = new HandoverAuthorResponse();

        response.handoverNoteId = note.getId();
        response.authorName = note.getAuthor().getUsername();
        response.recipientName = recipientName;
        response.title = note.getTitle();
        response.content = note.getContent();
        response.occurredAt = note.getCreatedAt();

        return response;
    }
}

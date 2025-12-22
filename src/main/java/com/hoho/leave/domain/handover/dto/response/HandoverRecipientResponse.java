package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 수신 인수인계 응답 DTO.
 */
@Data
public class HandoverRecipientResponse {

    /** 인수인계 ID */
    Long handoverId;

    /** 작성자 이름 */
    String authorName;

    /** 제목 */
    String title;

    /** 내용 */
    String content;

    /** 작성 일시 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    /**
     * 인수인계 노트로 응답 DTO를 생성한다.
     *
     * @param note 인수인계 노트 엔티티
     * @return 수신 인수인계 응답
     */
    public static HandoverRecipientResponse of(HandoverNote note) {
        HandoverRecipientResponse response = new HandoverRecipientResponse();

        response.handoverId = note.getId();
        response.authorName = note.getAuthor().getUsername();
        response.title = note.getTitle();
        response.content = note.getContent();
        response.occurredAt = note.getCreatedAt();

        return response;
    }
}

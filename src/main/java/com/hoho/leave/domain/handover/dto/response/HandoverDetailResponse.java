package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 인수인계 상세 응답 DTO.
 */
@Data
public class HandoverDetailResponse {

    /** 인수인계 ID */
    Long handoverId;

    /** 작성자 이름 */
    String authorName;

    /** 수신자 이름 목록 */
    List<String> recipientNames;

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
     * @param note           인수인계 노트 엔티티
     * @param recipientNames 수신자 이름 목록
     * @return 인수인계 상세 응답
     */
    public static HandoverDetailResponse of(HandoverNote note, List<String> recipientNames) {
        HandoverDetailResponse response = new HandoverDetailResponse();

        response.handoverId = note.getId();
        response.authorName = note.getAuthor().getUsername();
        response.recipientNames = recipientNames;
        response.title = note.getTitle();
        response.content = note.getContent();
        response.occurredAt = note.getCreatedAt();

        return response;
    }
}

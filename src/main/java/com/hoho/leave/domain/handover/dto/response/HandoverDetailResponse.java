package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HandoverDetailResponse {

    Long handoverId;

    String authorName;

    List<String> recipientNames;

    String title;

    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

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

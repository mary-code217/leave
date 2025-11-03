package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HandoverRecipientResponse {
    Long handoverId;

    String authorName;

    String title;

    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

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

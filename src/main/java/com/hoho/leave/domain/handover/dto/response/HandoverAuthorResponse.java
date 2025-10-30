package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverAuthorResponse {

    Long handoverNoteId;

    String authorName;

    List<String> recipientName;

    String title;

    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

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

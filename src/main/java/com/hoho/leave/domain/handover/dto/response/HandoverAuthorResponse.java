package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    public static HandoverAuthorResponse from(Long handoverNoteId, String authorName,
                                              List<String> recipientName, String title,
                                              String content, LocalDateTime occurredAt) {
        return new HandoverAuthorResponse(handoverNoteId, authorName, recipientName, title, content, occurredAt);
    }
}

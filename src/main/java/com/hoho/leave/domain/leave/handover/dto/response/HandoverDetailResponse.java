package com.hoho.leave.domain.leave.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverDetailResponse {

    Long handoverId;
    String authorName;
    List<String> recipientNames;
    String title;
    String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    public static HandoverDetailResponse from(Long handoverNoteId, String authorName,
                                              List<String> recipientNames, String title,
                                              String content, LocalDateTime occurredAt) {
        return new HandoverDetailResponse(handoverNoteId, authorName, recipientNames, title, content, occurredAt);
    }
}

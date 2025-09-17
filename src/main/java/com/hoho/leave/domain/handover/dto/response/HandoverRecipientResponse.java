package com.hoho.leave.domain.handover.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandoverRecipientResponse {
    Long handoverId;
    String authorName;
    String title;
    String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime occurredAt;

    public static HandoverRecipientResponse from(HandoverNote note) {
        return new HandoverRecipientResponse(
                note.getId(),
                note.getAuthor().getUsername(),
                note.getTitle(),
                note.getContent(),
                note.getCreatedAt()
        );
    }
}

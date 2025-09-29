package com.hoho.leave.domain.leave.request.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AttachmentResponse {
    Long attachmentId;
    String originalName;
    Long size;
    String uploadBy;
    LocalDateTime uploadAt;

    public static AttachmentResponse of(Long attachmentId, String originalName,
                                        Long size, String uploadBy, LocalDateTime uploadAt) {
        return AttachmentResponse.builder()
                .attachmentId(attachmentId)
                .originalName(originalName)
                .size(size)
                .uploadBy(uploadBy)
                .uploadAt(uploadAt).build();
    }
}

package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AttachmentResponse {

    Long attachmentId;

    String originalName;

    Long size;

    String uploadBy;

    LocalDateTime uploadAt;

    public static AttachmentResponse of(LeaveRequestAttachment attachment) {
        AttachmentResponse response = new AttachmentResponse();

        response.attachmentId = attachment.getId();
        response.originalName = attachment.getOriginalName();
        response.size = attachment.getSizeBytes();
        response.uploadBy = attachment.getUploadedBy().getUsername();
        response.uploadAt = attachment.getUpdatedAt();

        return response;
    }
}

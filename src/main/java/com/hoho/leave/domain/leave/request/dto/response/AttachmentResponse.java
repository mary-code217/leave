package com.hoho.leave.domain.leave.request.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 첨부파일 응답 DTO.
 * 
 * 휴가 신청서에 첨부된 파일의 정보를 담는다.
 * 
 */
@Data
public class AttachmentResponse {
    /**
     * 첨부파일 ID
     */
    Long attachmentId;

    /**
     * 원본 파일명
     */
    String originalName;

    /**
     * 저장된 파일명
     */
    String storeName;

    /**
     * 파일 크기 (bytes)
     */
    Long size;

    /**
     * 업로드한 사용자명
     */
    String uploadBy;

    /**
     * 업로드 시각
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime uploadAt;

    /**
     * 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param attachment 첨부파일 엔티티
     * @return 첨부파일 응답 DTO
     */
    public static AttachmentResponse of(LeaveRequestAttachment attachment) {
        AttachmentResponse response = new AttachmentResponse();

        response.attachmentId = attachment.getId();
        response.originalName = attachment.getOriginalName();
        response.storeName = attachment.getStoreName();
        response.size = attachment.getSizeBytes();
        response.uploadBy = attachment.getUploadedBy().getUsername();
        response.uploadAt = attachment.getUpdatedAt();

        return response;
    }
}

package com.hoho.leave.domain.leave.request.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 첨부파일 업로드 요청 DTO.
 * 
 * 휴가 신청서에 첨부파일을 업로드할 때 필요한 정보를 담는다.
 * 
 */
@Data
public class AttachmentUploadRequest {
    /**
     * 사용자 ID
     */
    @NotNull
    Long userId;

    /**
     * 휴가 신청 ID
     */
    @NotNull
    Long leaveRequestId;
}

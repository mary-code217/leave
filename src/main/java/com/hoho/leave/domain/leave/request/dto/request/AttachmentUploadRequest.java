package com.hoho.leave.domain.leave.request.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttachmentUploadRequest {
    @NotNull
    Long userId;

    @NotNull
    Long leaveRequestId;
}

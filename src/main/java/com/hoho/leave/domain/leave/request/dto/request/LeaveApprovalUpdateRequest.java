package com.hoho.leave.domain.leave.request.dto.request;

import com.hoho.leave.domain.leave.request.entity.ApprovalStatus;
import lombok.Data;

@Data
public class LeaveApprovalUpdateRequest {
    ApprovalStatus status;

    String comment;
}

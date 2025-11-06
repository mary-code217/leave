package com.hoho.leave.domain.leave.request.dto.request;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LeaveRequestUpdateRequest {
    @NotBlank
    LeaveRequestStatus status;
}

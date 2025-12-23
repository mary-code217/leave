package com.hoho.leave.domain.leave.request.dto.request;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 휴가 신청 상태 수정 요청 DTO.
 * 
 * 휴가 신청의 상태를 변경할 때 필요한 정보를 담는다.
 * 
 */
@Data
public class LeaveRequestUpdateRequest {
    /**
     * 휴가 신청 상태
     */
    @NotBlank
    LeaveRequestStatus status;
}

package com.hoho.leave.domain.leave.request.dto.request;

import com.hoho.leave.domain.leave.request.entity.ApprovalStatus;
import lombok.Data;

/**
 * 휴가 결재 상태 변경 요청 DTO.
 * 
 * 휴가 신청에 대한 결재 승인 또는 반려 시 필요한 정보를 담는다.
 * 
 */
@Data
public class LeaveApprovalUpdateRequest {
    /**
     * 결재 상태
     */
    ApprovalStatus status;

    /**
     * 결재 의견
     */
    String comment;
}

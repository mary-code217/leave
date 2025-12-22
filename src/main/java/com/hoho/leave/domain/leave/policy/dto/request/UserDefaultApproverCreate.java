package com.hoho.leave.domain.leave.policy.dto.request;

import lombok.Data;

/**
 * 사용자 기본 결재자 생성 요청 DTO.
 * <p>
 * 사용자별 기본 결재자를 설정하기 위한 요청 데이터를 담는다.
 * </p>
 */
@Data
public class UserDefaultApproverCreate {
    Long userId;

    Long approverId;

    Integer stepNo;
}

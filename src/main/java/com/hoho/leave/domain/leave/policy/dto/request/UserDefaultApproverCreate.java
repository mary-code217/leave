package com.hoho.leave.domain.leave.policy.dto.request;

import lombok.Data;

@Data
public class UserDefaultApproverCreate {
    Long userId;

    Long approverId;

    Integer stepNo;
}

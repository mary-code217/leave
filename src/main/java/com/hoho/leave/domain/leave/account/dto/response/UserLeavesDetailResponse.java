package com.hoho.leave.domain.leave.account.dto.response;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class UserLeavesDetailResponse {
    Long id;

    String username;

    String employeeNo;

    LeaveStage leaveStage;

    LocalDate nextAccrualAt;

    BigDecimal balanceDays;

    public static UserLeavesDetailResponse of(UserLeaves userLeaves) {
        UserLeavesDetailResponse response = new UserLeavesDetailResponse();

        response.id = userLeaves.getId();
        response.username = userLeaves.getUser().getUsername();
        response.employeeNo = userLeaves.getUser().getEmployeeNo();
        response.leaveStage = userLeaves.getLeaveStage();
        response.nextAccrualAt = userLeaves.getNextAccrualAt();
        response.balanceDays = userLeaves.getBalanceDays();

        return response;
    }
}
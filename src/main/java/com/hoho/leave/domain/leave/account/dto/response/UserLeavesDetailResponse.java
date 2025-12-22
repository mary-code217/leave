package com.hoho.leave.domain.leave.account.dto.response;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import com.hoho.leave.domain.leave.account.entity.UserLeaves;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 사용자 휴가 정보 상세 응답 DTO.
 * <p>
 * 사용자의 휴가 잔여 일수 및 휴가 단계 정보를 담는다.
 * </p>
 */
@Data
public class UserLeavesDetailResponse {
    Long id;

    String username;

    String employeeNo;

    LeaveStage leaveStage;

    LocalDate nextAccrualAt;

    BigDecimal balanceDays;

    /**
     * 사용자 휴가 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param userLeaves 사용자 휴가 엔티티
     * @return 사용자 휴가 정보 상세 응답
     */
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
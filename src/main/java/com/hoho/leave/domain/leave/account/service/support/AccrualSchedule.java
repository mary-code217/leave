package com.hoho.leave.domain.leave.account.service.support;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 휴가 부여 스케줄 DTO.
 * <p>
 * 휴가 부여 단계와 다음 부여일 정보를 담는다.
 * </p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccrualSchedule {
    LeaveStage leaveStage;
    LocalDate nextAccrualAt;
}

package com.hoho.leave.domain.leave.account.dto;

import com.hoho.leave.domain.leave.account.entity.LeaveStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountEvent {
    LeaveStage leaveStage;
    LocalDate nextAccrualAt;
}

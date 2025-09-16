package com.hoho.leave.domain.leave.policy.dto.request;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import com.hoho.leave.domain.org.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateConcurrencyPolicy {
    Team team;
    LeaveType leaveType;
    Integer maxConcurrent;
    LocalDate effectiveFrom;
    LocalDate effectiveTo;
}

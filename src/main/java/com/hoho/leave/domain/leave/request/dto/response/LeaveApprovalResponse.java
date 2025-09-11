package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.ApprovalStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LeaveApprovalResponse {
    Long approvalId;
    ApprovalStatus status;

    Long authorId;
    String authorName;
    BigDecimal quantityDays;
    LocalDate startDay;
    LocalDate endDay;
    LocalTime startTime;
    LocalTime endTime;

    public static LeaveApprovalResponse of(Long approvalId, ApprovalStatus status,
                                           Long authorId, String authorName,
                                           BigDecimal quantityDays, LocalDate startDay,
                                           LocalDate endDay, LocalTime startTime, LocalTime endTime) {
        return LeaveApprovalResponse.builder()
                .approvalId(approvalId)
                .status(status)
                .authorId(authorId)
                .authorName(authorName)
                .quantityDays(quantityDays)
                .startDay(startDay)
                .endDay(endDay)
                .startTime(startTime)
                .endTime(endTime).build();
    }
}

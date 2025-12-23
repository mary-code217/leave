package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.ApprovalStatus;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestApproval;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 휴가 결재 응답 DTO.
 * 
 * 휴가 신청의 결재 정보를 담는다.
 * 
 */
@Data
public class LeaveApprovalResponse {
    /**
     * 결재 ID
     */
    Long approvalId;

    /**
     * 결재 상태
     */
    ApprovalStatus status;

    /**
     * 신청자 ID
     */
    Long authorId;

    /**
     * 신청자명
     */
    String authorName;

    /**
     * 사용 일수
     */
    BigDecimal quantityDays;

    /**
     * 시작일
     */
    LocalDate startDay;

    /**
     * 종료일
     */
    LocalDate endDay;

    /**
     * 시작 시간
     */
    LocalTime startTime;

    /**
     * 종료 시간
     */
    LocalTime endTime;

    /**
     * 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param request 휴가 결재 엔티티
     * @return 휴가 결재 응답 DTO
     */
    public static LeaveApprovalResponse of(LeaveRequestApproval request) {
        LeaveApprovalResponse response = new LeaveApprovalResponse();

        response.approvalId = request.getId();
        response.status = request.getStatus();
        response.authorId = request.getLeaveRequest().getUser().getId();
        response.authorName = request.getLeaveRequest().getUser().getUsername();
        response.quantityDays = request.getLeaveRequest().getQuantityDays();
        response.startDay = request.getLeaveRequest().getStartDay();
        response.endDay = request.getLeaveRequest().getEndDay();
        response.startTime = request.getLeaveRequest().getStartTime();
        response.endTime = request.getLeaveRequest().getEndTime();

        return response;
    }
}

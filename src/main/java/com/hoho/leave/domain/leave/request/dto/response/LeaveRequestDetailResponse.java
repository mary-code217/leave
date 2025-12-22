package com.hoho.leave.domain.leave.request.dto.response;

import com.hoho.leave.domain.leave.request.entity.LeaveRequest;
import com.hoho.leave.domain.leave.request.entity.LeaveRequestStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 휴가 신청 상세 응답 DTO.
 * <p>
 * 휴가 신청의 상세 정보를 담는다.
 * </p>
 */
@Data
public class LeaveRequestDetailResponse {
    /**
     * 휴가 신청 ID
     */
    Long leaveRequestId;

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
     * 휴가 신청 상태
     */
    LeaveRequestStatus status;

    /**
     * 사용자 ID
     */
    Long userId;

    /**
     * 사용자명
     */
    String username;

    /**
     * 휴가 유형 ID
     */
    Long leaveTypeId;

    /**
     * 휴가 유형명
     */
    String leaveTypeName;

    /**
     * 첨부파일 존재 여부
     */
    boolean hasAttachment;

    /**
     * 첨부파일 목록
     */
    List<AttachmentResponse> attachments;

    /**
     * 엔티티로부터 응답 DTO를 생성한다.
     *
     * @param leaveRequest 휴가 신청 엔티티
     * @return 휴가 신청 상세 응답 DTO
     */
    public static LeaveRequestDetailResponse of(LeaveRequest leaveRequest) {
        LeaveRequestDetailResponse response = new LeaveRequestDetailResponse();

        response.leaveRequestId = leaveRequest.getId();
        response.startDay = leaveRequest.getStartDay();
        response.endDay = leaveRequest.getEndDay();
        response.startTime = leaveRequest.getStartTime();
        response.endTime = leaveRequest.getEndTime();
        response.status = leaveRequest.getStatus();

        response.userId = leaveRequest.getUser().getId();
        response.username = leaveRequest.getUser().getUsername();

        response.leaveTypeId = leaveRequest.getLeaveType().getId();
        response.leaveTypeName = leaveRequest.getLeaveType().getLeaveName();

        return response;
    }

    /**
     * 첨부파일 목록을 추가한다.
     *
     * @param attachments 첨부파일 목록
     */
    public void addAttachments(List<AttachmentResponse> attachments) {
        this.attachments = attachments;
    }

    /**
     * 첨부파일 존재 여부를 설정한다.
     *
     * @param hasAttachment 첨부파일 존재 여부
     */
    public void hasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }
}

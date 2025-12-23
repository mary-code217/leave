package com.hoho.leave.domain.leave.request.controller;

import com.hoho.leave.domain.leave.request.dto.request.LeaveApprovalUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalListResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveApprovalResponse;
import com.hoho.leave.domain.leave.request.service.LeaveRequestApprovalService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 휴가 결재 컨트롤러.
 * 
 * 휴가 신청의 결재 승인, 반려 등 결재 관련 기능을 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/approval")
public class LeaveRequestApprovalController {

    private final LeaveRequestApprovalService leaveRequestApprovalService;

    /**
     * 휴가 결재 상태를 변경한다.
     *
     * @param approvalId 결재 ID
     * @param updateRequest 결재 상태 변경 요청
     * @return 상태 변경 성공 메시지
     */
    @PutMapping("/{approvalId}")
    public ResponseEntity<?> updateLeaveApproval(@PathVariable Long approvalId,
                                                 @RequestBody LeaveApprovalUpdateRequest updateRequest) {

        leaveRequestApprovalService.updateLeaveApproval(approvalId, updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 신청 상태 변경");
    }

    /**
     * 특정 휴가 결재 정보를 조회한다.
     *
     * @param approvalId 결재 ID
     * @return 휴가 결재 응답
     */
    @GetMapping("/{approvalId}")
    public ResponseEntity<LeaveApprovalResponse> getLeaveApproval(@PathVariable Long approvalId) {

        LeaveApprovalResponse response = leaveRequestApprovalService.getLeaveApproval(approvalId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 특정 결재자의 모든 휴가 결재 목록을 조회한다.
     *
     * @param approverId 결재자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 결재 목록 응답
     */
    @GetMapping("{approverId}")
    public ResponseEntity<LeaveApprovalListResponse> getAllLeaveApproval(
            @PathVariable Long approverId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        LeaveApprovalListResponse response = leaveRequestApprovalService.getAllLeaveApproval(approverId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/approval")
public class LeaveRequestApprovalController {

    private final LeaveRequestApprovalService leaveRequestApprovalService;

    @PutMapping("/{approvalId}")
    public ResponseEntity<?> updateLeaveApproval(@PathVariable Long approvalId,
                                                 @RequestBody LeaveApprovalUpdateRequest updateRequest) {

        leaveRequestApprovalService.updateLeaveApproval(approvalId, updateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 신청 상태 변경");
    }

    @GetMapping("/{approvalId}")
    public ResponseEntity<LeaveApprovalResponse> getLeaveApproval(@PathVariable Long approvalId) {

        LeaveApprovalResponse response = leaveRequestApprovalService.getLeaveApproval(approvalId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("{approverId}")
    public ResponseEntity<LeaveApprovalListResponse> getAllLeaveApproval(
            @PathVariable Long approverId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        LeaveApprovalListResponse response = leaveRequestApprovalService.getAllLeaveApproval(approverId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}

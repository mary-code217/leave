package com.hoho.leave.domain.leave.request.controller;

import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestCreateRequest;
import com.hoho.leave.domain.leave.request.dto.request.LeaveRequestUpdateRequest;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestDetailResponse;
import com.hoho.leave.domain.leave.request.dto.response.LeaveRequestListResponse;
import com.hoho.leave.domain.leave.request.service.LeaveRequestService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    @PostMapping("")
    public ResponseEntity<?> createLeaveRequest(@RequestBody LeaveRequestCreateRequest req) {

        leaveRequestService.createLeaveRequest(req);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 신청 완료");
    }

    @PutMapping("/{leaveRequestId}")
    public ResponseEntity<?> updateStatus(@PathVariable("leaveRequestId") Long leaveRequestId,
                                          @RequestBody LeaveRequestUpdateRequest req) {

        leaveRequestService.updateLeaveRequest(leaveRequestId, req);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 상태 변경");
    }
    
    @DeleteMapping("/{leaveRequestId}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable("leaveRequestId") Long leaveRequestId) {

        leaveRequestService.deleteLeaveRequest(leaveRequestId);

        return ResponseEntity.status(HttpStatus.OK).body("신청 취소 완료");
    }

    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<LeaveRequestDetailResponse> getLeaveRequest(
            @PathVariable("leaveRequestId") Long leaveRequestId) {

        LeaveRequestDetailResponse response = leaveRequestService.getLeaveRequest(leaveRequestId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<LeaveRequestListResponse> getUserLeaveRequest(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveRequestListResponse response = leaveRequestService.getUserLeaveRequests(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<LeaveRequestListResponse> getAllLeaveRequests(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveRequestListResponse response = leaveRequestService.getAllLeaveRequests(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

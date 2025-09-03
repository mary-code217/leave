package com.hoho.leave.domain.leave.request.controller;

import com.hoho.leave.domain.leave.request.service.LeaveRequestApprovalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/approval")
public class LeaveRequestApprovalController {

    private final LeaveRequestApprovalService leaveRequestApprovalService;

    @PostMapping("")
    public ResponseEntity<?> createApproval() {

        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}

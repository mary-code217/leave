package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.policy.dto.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.service.LeaveTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/policy")
public class LeavePolicyController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping("")
    public ResponseEntity<?> createLeaveType(@RequestBody LeaveTypeCreateRequest leaveTypeCreateRequest) {

        leaveTypeService.LeaveTypeCreate(leaveTypeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 생성");
    }

    @DeleteMapping("/{leaveTypeId}")
    public ResponseEntity<?> deleteLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 삭제");
    }

}

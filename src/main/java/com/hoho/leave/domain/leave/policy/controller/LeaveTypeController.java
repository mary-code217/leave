package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeDetailResponse;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeListResponse;
import com.hoho.leave.domain.leave.policy.service.LeaveTypeService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/policy")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    @PostMapping("")
    public ResponseEntity<?> createLeaveType(@RequestBody LeaveTypeCreateRequest leaveTypeCreateRequest) {

        leaveTypeService.LeaveTypeCreate(leaveTypeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 생성");
    }
    
    @PutMapping("/{leaveTypeId}")
    public ResponseEntity<?> updateLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId,
                                             @RequestBody LeaveTypeUpdateRequest leaveTypeUpdate) {

        leaveTypeService.LeaveTypeUpdate(leaveTypeId, leaveTypeUpdate);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 변경");
    }

    @DeleteMapping("/{leaveTypeId}")
    public ResponseEntity<?> deleteLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {

        leaveTypeService.LeaveTypeDelete(leaveTypeId);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 삭제");
    }

    @GetMapping("/{leaveTypeId}")
    public ResponseEntity<LeaveTypeDetailResponse> getLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {

        LeaveTypeDetailResponse response = leaveTypeService.getLeaveType(leaveTypeId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<LeaveTypeListResponse> getAllLeaveTypes(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                                                  @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveTypeListResponse response = leaveTypeService.getAllLeaveTypes(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

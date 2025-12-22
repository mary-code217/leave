package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeCreateRequest;
import com.hoho.leave.domain.leave.policy.dto.request.LeaveTypeUpdateRequest;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeDetailResponse;
import com.hoho.leave.domain.leave.policy.dto.response.LeaveTypeListResponse;
import com.hoho.leave.domain.leave.policy.service.LeaveTypeService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 휴가 유형 컨트롤러.
 * <p>
 * 휴가 유형의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/policy")
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService;

    /**
     * 휴가 유형을 생성한다.
     *
     * @param leaveTypeCreateRequest 휴가 유형 생성 요청
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createLeaveType(@RequestBody @Valid LeaveTypeCreateRequest leaveTypeCreateRequest) {

        leaveTypeService.createLeaveType(leaveTypeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 생성");
    }
    
    /**
     * 휴가 유형을 수정한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @param leaveTypeUpdate 휴가 유형 수정 요청
     * @return 성공 메시지
     */
    @PutMapping("/{leaveTypeId}")
    public ResponseEntity<?> updateLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId,
                                             @RequestBody @Valid LeaveTypeUpdateRequest leaveTypeUpdate) {

        leaveTypeService.updateLeaveType(leaveTypeId, leaveTypeUpdate);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 변경");
    }

    /**
     * 휴가 유형을 삭제한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{leaveTypeId}")
    public ResponseEntity<?> deleteLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {

        leaveTypeService.deleteLeaveType(leaveTypeId);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 유형 삭제");
    }

    /**
     * 휴가 유형 상세 정보를 조회한다.
     *
     * @param leaveTypeId 휴가 유형 ID
     * @return 휴가 유형 상세 응답
     */
    @GetMapping("/{leaveTypeId}")
    public ResponseEntity<LeaveTypeDetailResponse> getLeaveType(@PathVariable("leaveTypeId") Long leaveTypeId) {

        LeaveTypeDetailResponse response = leaveTypeService.getLeaveType(leaveTypeId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 휴가 유형 목록을 조회한다.
     *
     * @param page 페이지 번호 (기본값: 1, 최소값: 1)
     * @param size 페이지 크기 (기본값: 5, 최소값: 1, 최대값: 20)
     * @return 휴가 유형 목록 응답
     */
    @GetMapping("")
    public ResponseEntity<LeaveTypeListResponse> getAllLeaveTypes(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                                                  @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveTypeListResponse response = leaveTypeService.getAllLeaveTypes(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

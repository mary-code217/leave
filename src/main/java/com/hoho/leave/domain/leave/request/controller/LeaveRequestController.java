package com.hoho.leave.domain.leave.request.controller;

import com.hoho.leave.domain.leave.facade.LeaveRequestModifyFacade;
import com.hoho.leave.domain.leave.facade.LeaveRequestQueryFacade;
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

/**
 * 휴가 신청 컨트롤러.
 * 
 * 휴가 신청의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leave/request")
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;
    private final LeaveRequestQueryFacade leaveRequestQueryFacade;
    private final LeaveRequestModifyFacade leaveRequestModifyFacade;

    /**
     * 휴가 신청을 생성한다.
     *
     * @param req 휴가 신청 생성 요청
     * @return 휴가 신청 완료 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createLeaveRequest(@RequestBody LeaveRequestCreateRequest req) {

        leaveRequestService.createLeaveRequest(req);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 신청 완료");
    }

    /**
     * 휴가 신청 상태를 수정한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @param req 휴가 신청 수정 요청
     * @return 상태 변경 성공 메시지
     */
    @PutMapping("/{leaveRequestId}")
    public ResponseEntity<?> updateStatus(@PathVariable("leaveRequestId") Long leaveRequestId,
                                          @RequestBody LeaveRequestUpdateRequest req) {

        leaveRequestService.updateLeaveRequest(leaveRequestId, req);

        return ResponseEntity.status(HttpStatus.OK).body("휴가 상태 변경");
    }

    /**
     * 휴가 신청을 취소한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 신청 취소 완료 메시지
     */
    @DeleteMapping("/{leaveRequestId}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable("leaveRequestId") Long leaveRequestId) {

        leaveRequestModifyFacade.deleteLeaveRequest(leaveRequestId);

        return ResponseEntity.status(HttpStatus.OK).body("신청 취소 완료");
    }

    /**
     * 특정 휴가 신청의 상세 정보를 조회한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 휴가 신청 상세 응답
     */
    @GetMapping("/{leaveRequestId}")
    public ResponseEntity<LeaveRequestDetailResponse> getLeaveRequest(
            @PathVariable("leaveRequestId") Long leaveRequestId) {

        LeaveRequestDetailResponse response = leaveRequestQueryFacade.getLeaveRequest(leaveRequestId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 특정 사용자의 휴가 신청 목록을 조회한다.
     *
     * @param userId 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 신청 목록 응답
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<LeaveRequestListResponse> getUserLeaveRequest(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveRequestListResponse response = leaveRequestService.getUserLeaveRequests(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 모든 휴가 신청 목록을 조회한다.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 휴가 신청 목록 응답
     */
    @GetMapping("")
    public ResponseEntity<LeaveRequestListResponse> getAllLeaveRequests(
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size) {

        LeaveRequestListResponse response = leaveRequestService.getAllLeaveRequests(page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

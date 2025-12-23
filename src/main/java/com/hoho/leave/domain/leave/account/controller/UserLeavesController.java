package com.hoho.leave.domain.leave.account.controller;

import com.hoho.leave.domain.leave.account.dto.response.UserLeavesDetailResponse;
import com.hoho.leave.domain.leave.account.service.LeaveAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 휴가 정보 컨트롤러.
 * 
 * 사용자의 휴가 잔여 일수 및 휴가 계정 정보를 조회한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaves")
public class UserLeavesController {

    private final LeaveAccountService leaveAccountService;

    /**
     * 특정 사용자의 휴가 정보를 조회한다.
     *
     * @param userId 사용자 ID
     * @return 사용자 휴가 정보 응답
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserLeaves(@PathVariable Long userId) {

        UserLeavesDetailResponse response = leaveAccountService.getUserLeaves(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

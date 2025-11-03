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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaves")
public class UserLeavesController {

    private final LeaveAccountService leaveAccountService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserLeaves(@PathVariable Long userId) {

        UserLeavesDetailResponse response = leaveAccountService.getUserLeaves(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

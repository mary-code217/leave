package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.policy.service.UserDefaultApproverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 사용자 기본 결재자 컨트롤러.
 * 
 * 사용자별 기본 결재자 설정 기능을 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/default")
public class UserDefaultApproverController {

    private final UserDefaultApproverService userDefaultApproverService;

}

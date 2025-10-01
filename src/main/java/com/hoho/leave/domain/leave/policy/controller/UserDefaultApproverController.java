package com.hoho.leave.domain.leave.policy.controller;

import com.hoho.leave.domain.leave.policy.service.UserDefaultApproverService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/default")
public class UserDefaultApproverController {

    private final UserDefaultApproverService userDefaultApproverService;

}

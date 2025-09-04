package com.hoho.leave.domain.audit.controller;

import com.hoho.leave.domain.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
public class AuditLogController {

    private final AuditLogService auditLogService;



}

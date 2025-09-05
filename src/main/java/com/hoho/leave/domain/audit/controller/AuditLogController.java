package com.hoho.leave.domain.audit.controller;

import com.hoho.leave.domain.audit.dto.response.AuditLogListResponse;
import com.hoho.leave.domain.audit.service.AuditLogService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping("")
    public ResponseEntity<AuditLogListResponse> getLogs(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
                                                        @RequestParam(defaultValue = "all") String objectType) {

        AuditLogListResponse response = auditLogService.getLogs(page, size, objectType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

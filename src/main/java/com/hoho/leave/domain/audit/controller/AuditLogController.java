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

/**
 * 감사 로그 조회 API 컨트롤러.
 * <p>
 * 시스템에서 발생한 모든 감사 로그를 조회하는 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/log")
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * 감사 로그 목록을 페이징하여 조회한다.
     *
     * @param page       페이지 번호 (1부터 시작)
     * @param size       페이지당 항목 수 (1~20)
     * @param objectType 대상 객체 유형 필터 ("all"이면 전체 조회)
     * @return 감사 로그 목록 응답
     */
    @GetMapping("")
    public ResponseEntity<AuditLogListResponse> getAllLogs(@RequestParam(defaultValue = "1") @Min(1) Integer page,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size,
                                                        @RequestParam(defaultValue = "all") String objectType) {

        AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

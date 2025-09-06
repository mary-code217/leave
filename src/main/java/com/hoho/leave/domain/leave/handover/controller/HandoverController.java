package com.hoho.leave.domain.leave.handover.controller;

import com.hoho.leave.domain.leave.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.leave.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.leave.handover.service.HandoverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/handover")
public class HandoverController {

    private final HandoverService handoverService;

    @PostMapping("")
    public ResponseEntity<?> createHandoverNote(@RequestBody @Valid HandoverCreateRequest handoverCreateRequest) {
        
        handoverService.createHandover(handoverCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("인수인계 등록 성공");
    }

    // 발신목록
    @GetMapping("/user/{userId}")
    public ResponseEntity<HandoverAuthorListResponse> getHandoverAuthorList(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        HandoverAuthorListResponse response = handoverService.getHandoverAuthorList(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    // 수신목록
}

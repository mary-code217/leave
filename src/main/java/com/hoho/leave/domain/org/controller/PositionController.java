package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/position")
public class PositionController {

    private final PositionService positionService;

    @PostMapping("")
    public ResponseEntity<?> createPosition(@RequestBody @Valid PositionCreateRequest positionCreateRequest) {

        positionService.createPosition(positionCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body("직책 등록 성공");
    }

}

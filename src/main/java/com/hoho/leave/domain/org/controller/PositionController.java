package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.request.PositionUpdateRequest;
import com.hoho.leave.domain.org.dto.response.PositionDetailResponse;
import com.hoho.leave.domain.org.dto.response.PositionListResponse;
import com.hoho.leave.domain.org.service.PositionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/position")
public class PositionController {

    private final PositionService positionService;

    @PostMapping("")
    public ResponseEntity<?> createPosition(@RequestBody @Valid PositionCreateRequest positionCreateRequest) {

        positionService.createPosition(positionCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직책 등록 성공");
    }
    
    @PutMapping("/{positionId}")
    public ResponseEntity<?> updatePosition(@PathVariable Long positionId, 
                                            @RequestBody @Valid PositionUpdateRequest positionUpdateRequest) {

        positionService.updatePosition(positionId, positionUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직책 수정 성공");
    }

    @DeleteMapping("/{positionId}")
    public ResponseEntity<?> deletePosition(@PathVariable Long positionId) {

        positionService.deletePosition(positionId);

        return ResponseEntity.status(HttpStatus.OK).body("직책 삭제 성공");
    }

    @GetMapping("/{positionId}")
    public ResponseEntity<PositionDetailResponse> getPosition(@PathVariable Long positionId) {

        PositionDetailResponse response = positionService.getPosition(positionId);

        return new ResponseEntity<PositionDetailResponse>(response, HttpStatus.OK);
    }

    @Validated
    @GetMapping("")
    public ResponseEntity<PositionListResponse> getAllPositions(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                                @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        PositionListResponse response = positionService.getAllPositions(size, page);

        return new ResponseEntity<PositionListResponse>(response, HttpStatus.OK);
    }

}

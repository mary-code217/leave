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

/**
 * 직책 관리 컨트롤러.
 * 
 * 직책의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/position")
public class PositionController {

    private final PositionService positionService;

    /**
     * 직책을 생성한다.
     *
     * @param positionCreateRequest 직책 생성 요청 정보
     * @return 생성 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createPosition(@RequestBody @Valid PositionCreateRequest positionCreateRequest) {

        positionService.createPosition(positionCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직책 등록 성공");
    }

    /**
     * 직책을 수정한다.
     *
     * @param positionId 직책 ID
     * @param positionUpdateRequest 직책 수정 요청 정보
     * @return 수정 성공 메시지
     */
    @PutMapping("/{positionId}")
    public ResponseEntity<?> updatePosition(@PathVariable Long positionId,
                                            @RequestBody @Valid PositionUpdateRequest positionUpdateRequest) {

        positionService.updatePosition(positionId, positionUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("직책 수정 성공");
    }

    /**
     * 직책을 삭제한다.
     *
     * @param positionId 직책 ID
     * @return 삭제 성공 메시지
     */
    @DeleteMapping("/{positionId}")
    public ResponseEntity<?> deletePosition(@PathVariable Long positionId) {

        positionService.deletePosition(positionId);

        return ResponseEntity.status(HttpStatus.OK).body("직책 삭제 성공");
    }

    /**
     * 특정 직책을 조회한다.
     *
     * @param positionId 직책 ID
     * @return 직책 상세 정보
     */
    @GetMapping("/{positionId}")
    public ResponseEntity<PositionDetailResponse> getPosition(@PathVariable Long positionId) {

        PositionDetailResponse response = positionService.getPosition(positionId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 전체 직책 목록을 조회한다.
     *
     * @param size 페이지 크기 (기본값: 5, 최소: 1, 최대: 20)
     * @param page 페이지 번호 (기본값: 1, 최소: 1)
     * @return 직책 목록 및 페이징 정보
     */
    @Validated
    @GetMapping("")
    public ResponseEntity<PositionListResponse> getAllPositions(@RequestParam(defaultValue = "5") @Min(1) @Max(20) Integer size,
                                                                @RequestParam(defaultValue = "1") @Min(1) Integer page) {

        PositionListResponse response = positionService.getAllPositions(size, page);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

package com.hoho.leave.domain.handover.controller;

import com.hoho.leave.domain.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.handover.dto.request.HandoverUpdateRequest;
import com.hoho.leave.domain.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverDetailResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverRecipientListResponse;
import com.hoho.leave.domain.handover.facade.HandoverModify;
import com.hoho.leave.domain.handover.service.HandoverRecipientService;
import com.hoho.leave.domain.handover.service.HandoverService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 인수인계 관리 API 컨트롤러.
 * <p>
 * 휴가 시 업무 인수인계 노트의 생성, 조회, 수정, 삭제 기능을 제공한다.
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/handover")
public class HandoverController {

    private final HandoverRecipientService recipientService;
    private final HandoverService handoverService;
    private final HandoverModify handoverModify;

    /**
     * 인수인계를 생성한다.
     *
     * @param handoverCreateRequest 인수인계 생성 요청
     * @return 성공 메시지
     */
    @PostMapping("")
    public ResponseEntity<?> createHandover(@RequestBody @Valid HandoverCreateRequest handoverCreateRequest) {

        handoverModify.createHandover(handoverCreateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("인수인계 등록 성공");
    }

    /**
     * 사용자가 발신한 인수인계 목록을 조회한다.
     *
     * @param userId 발신자 ID
     * @param page   페이지 번호
     * @param size   페이지 크기
     * @return 발신 인수인계 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<HandoverAuthorListResponse> getHandoverAuthorList(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        HandoverAuthorListResponse response = handoverService.getHandoverAuthorList(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 사용자가 수신한 인수인계 목록을 조회한다.
     *
     * @param recipientId 수신자 ID
     * @param page        페이지 번호
     * @param size        페이지 크기
     * @return 수신 인수인계 목록
     */
    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<HandoverRecipientListResponse> getHandoverRecipientList(
            @PathVariable("recipientId") Long recipientId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        HandoverRecipientListResponse response = recipientService.getRecipientList(recipientId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 인수인계를 단건 조회한다.
     *
     * @param handoverId 인수인계 ID
     * @return 인수인계 상세 정보
     */
    @GetMapping("/{handoverId}")
    public ResponseEntity<HandoverDetailResponse> getHandover(@PathVariable("handoverId") Long handoverId) {

        HandoverDetailResponse response = handoverService.getHandover(handoverId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 인수인계를 수정한다.
     *
     * @param handoverId            인수인계 ID
     * @param handoverUpdateRequest 수정 요청
     * @return 성공 메시지
     */
    @PutMapping("/{handoverId}")
    public ResponseEntity<?> updateHandover(
            @PathVariable("handoverId") Long handoverId,
            @RequestBody @Valid HandoverUpdateRequest handoverUpdateRequest) {

        handoverModify.updateHandover(handoverId, handoverUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK).body("인수인계 수정 완료");
    }

    /**
     * 인수인계를 삭제한다.
     *
     * @param handoverId 인수인계 ID
     * @return 성공 메시지
     */
    @DeleteMapping("/{handoverId}")
    public ResponseEntity<?> deleteHandover(@PathVariable("handoverId") Long handoverId) {

        handoverModify.deleteHandover(handoverId);

        return ResponseEntity.status(HttpStatus.OK).body("인수인계 삭제 성공");
    }
}

package com.hoho.leave.domain.notification.controller;

import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.service.NotificationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 알림 컨트롤러.
 * 
 * 사용자 알림 조회 및 읽음 처리 API를 제공한다.
 * 
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 사용자의 모든 알림을 페이지네이션하여 조회한다.
     *
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호 (기본값: 1, 최소값: 1)
     * @param size 페이지 크기 (기본값: 10, 최소값: 1, 최대값: 20)
     * @return 알림 목록과 페이지 정보를 포함한 응답
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<NotificationListResponse> getAllNotifications(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        NotificationListResponse response = notificationService.getAllUserNotifications(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * 특정 알림을 읽음 처리한다.
     *
     * @param notificationId 읽음 처리할 알림 ID
     * @return 읽음 처리 완료 메시지
     */
    @PutMapping("/{notificationId}")
    public ResponseEntity<?> updateReadAt(@PathVariable("notificationId") Long notificationId) {

        notificationService.updateReadAt(notificationId);

        return ResponseEntity.status(HttpStatus.OK).body("읽음 처리 완료");
    }
}

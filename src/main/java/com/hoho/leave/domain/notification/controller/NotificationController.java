package com.hoho.leave.domain.notification.controller;

import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.service.NotificationService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<NotificationListResponse> getAllNotifications(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "1") @Min(1) Integer page,
            @RequestParam(defaultValue = "10") @Min(1) @Max(20) Integer size) {

        NotificationListResponse response = notificationService.getAllUserNotifications(userId, page, size);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{notificationId}")
    public ResponseEntity<?> updateReadAt(@PathVariable("notificationId") Long notificationId) {

        notificationService.updateReadAt(notificationId);

        return ResponseEntity.status(HttpStatus.OK).body("읽음 처리 완료");
    }
}

package com.hoho.leave.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.notification.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDetailResponse {

    Long notificationId;
    String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime readAt;

    public static NotificationDetailResponse from(Notification notification) {
        return NotificationDetailResponse.builder()
                .notificationId(notification.getId())
                .content(notification.getContent())
                .createAt(notification.getCreatedAt())
                .readAt(notification.getReadAt())
                .build();
    }
}

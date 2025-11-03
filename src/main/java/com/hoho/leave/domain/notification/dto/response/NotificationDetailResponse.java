package com.hoho.leave.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.notification.entity.Notification;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDetailResponse {
    Long notificationId;

    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime readAt;

    public static NotificationDetailResponse of(Notification notification) {
        NotificationDetailResponse response = new NotificationDetailResponse();

        response.notificationId = notification.getId();
        response.content = notification.getContent();
        response.createAt = notification.getCreatedAt();
        response.readAt = notification.getReadAt();

        return response;
    }
}

package com.hoho.leave.domain.notification.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hoho.leave.domain.notification.entity.Notification;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 알림 상세 응답 DTO.
 * 
 * 개별 알림의 상세 정보를 클라이언트에 전달한다.
 * 
 */
@Data
public class NotificationDetailResponse {
    Long notificationId;

    String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime createAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    LocalDateTime readAt;

    /**
     * Notification 엔티티로부터 NotificationDetailResponse를 생성한다.
     *
     * @param notification 알림 엔티티
     * @return 생성된 알림 상세 응답 DTO
     */
    public static NotificationDetailResponse of(Notification notification) {
        NotificationDetailResponse response = new NotificationDetailResponse();

        response.notificationId = notification.getId();
        response.content = notification.getContent();
        response.createAt = notification.getCreatedAt();
        response.readAt = notification.getReadAt();

        return response;
    }
}

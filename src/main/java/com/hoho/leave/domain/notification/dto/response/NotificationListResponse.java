package com.hoho.leave.domain.notification.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class NotificationListResponse {
    Integer page;

    Integer size;

    List<NotificationDetailResponse> notifications;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    public static NotificationListResponse of(Page<?> page, List<NotificationDetailResponse> notifications) {
        NotificationListResponse response = new NotificationListResponse();

        response.page = page.getNumber()+1;
        response.size = page.getSize();
        response.notifications = notifications;
        response.totalPage = page.getTotalPages();
        response.totalElement = page.getTotalElements();
        response.firstPage = page.isFirst();
        response.lastPage = page.isLast();

        return response;
    }
}

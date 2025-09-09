package com.hoho.leave.domain.notification.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationListResponse {
    Integer page;
    Integer size;
    List<NotificationDetailResponse> notifications;
    Integer totalPage;
    Long totalElement;
    Boolean firstPage;
    Boolean lastPage;

    public static NotificationListResponse from(Integer page, Integer size,
                                                List<NotificationDetailResponse> notifications,
                                                Integer totalPage, Long totalElement,
                                                Boolean firstPage, Boolean lastPage) {
        return NotificationListResponse.builder()
                .page(page)
                .size(size)
                .notifications(notifications)
                .totalPage(totalPage)
                .totalElement(totalElement)
                .firstPage(firstPage)
                .lastPage(lastPage)
                .build();
    }
}

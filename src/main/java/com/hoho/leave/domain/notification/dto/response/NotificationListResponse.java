package com.hoho.leave.domain.notification.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 알림 목록 응답 DTO.
 * <p>
 * 페이지네이션된 알림 목록과 페이징 정보를 클라이언트에 전달한다.
 * </p>
 */
@Data
public class NotificationListResponse {
    Integer page;

    Integer size;

    List<NotificationDetailResponse> notifications;

    Integer totalPage;

    Long totalElement;

    Boolean firstPage;

    Boolean lastPage;

    /**
     * Page 객체와 알림 목록으로부터 NotificationListResponse를 생성한다.
     *
     * @param page 페이지 객체
     * @param notifications 알림 상세 응답 목록
     * @return 생성된 알림 목록 응답 DTO
     */
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

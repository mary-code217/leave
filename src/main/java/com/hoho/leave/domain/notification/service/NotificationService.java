package com.hoho.leave.domain.notification.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.notification.dto.response.NotificationDetailResponse;
import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.entity.Notification;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.notification.repository.NotificationRepository;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void createNotification(User user, NotificationType type, String content) {
        notificationRepository.save(Notification.create(user, type, content));
    }

    public void createManyNotification(List<User> recipients, NotificationType type, String content) {
        recipients.forEach(u -> createNotification(u, type, content));
    }

    @Transactional
    public void updateReadAt(Long NotificationId) {
        Notification notification = getNotificationEntity(NotificationId);

        notification.updateReadAt();
    }

    @Transactional(readOnly = true)
    public NotificationListResponse getAllUserNotifications(Long userId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);

        Page<Notification> pageList = notificationRepository.findByRecipientId(userId, pageable);

        List<NotificationDetailResponse> list = pageList.getContent().stream()
                .map(NotificationDetailResponse::of)
                .toList();

        return NotificationListResponse.of(pageList, list);
    }

    /**
     * 페이지 정보 생성
     */
    private PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
    }

    /**
     * 알림 엔티티 조회
     */
    private Notification getNotificationEntity(Long NotificationId) {
        return notificationRepository.findById(NotificationId)
                .orElseThrow(() -> new NotFoundException("Not Found Notification : " + NotificationId));
    }
}

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

/**
 * 알림 서비스.
 * 
 * 알림 생성, 조회, 읽음 처리 등의 비즈니스 로직을 제공한다.
 * 
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * 단일 사용자에게 알림을 생성한다.
     *
     * @param user 알림을 받을 사용자
     * @param type 알림 유형
     * @param content 알림 내용
     */
    public void createNotification(User user, NotificationType type, String content) {
        notificationRepository.save(Notification.create(user, type, content));
    }

    /**
     * 여러 사용자에게 동일한 알림을 생성한다.
     *
     * @param recipients 알림을 받을 사용자 목록
     * @param type 알림 유형
     * @param content 알림 내용
     */
    public void createManyNotification(List<User> recipients, NotificationType type, String content) {
        recipients.forEach(u -> createNotification(u, type, content));
    }

    /**
     * 특정 알림을 읽음 처리한다.
     *
     * @param NotificationId 읽음 처리할 알림 ID
     */
    @Transactional
    public void updateReadAt(Long NotificationId) {
        Notification notification = getNotificationEntity(NotificationId);

        notification.updateReadAt();
    }

    /**
     * 사용자의 모든 알림을 페이지네이션하여 조회한다.
     *
     * @param userId 조회할 사용자 ID
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 알림 목록과 페이징 정보를 포함한 응답
     */
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
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 생성된 페이지 요청 객체
     */
    private PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
    }

    /**
     * 알림 엔티티 조회
     *
     * @param NotificationId 조회할 알림 ID
     * @return 조회된 알림 엔티티
     * @throws NotFoundException 알림을 찾을 수 없는 경우
     */
    private Notification getNotificationEntity(Long NotificationId) {
        return notificationRepository.findById(NotificationId)
                .orElseThrow(() -> new NotFoundException("Not Found Notification : " + NotificationId));
    }
}

package com.hoho.leave.domain.notification.service;

import com.hoho.leave.domain.notification.dto.response.NotificationDetailResponse;
import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.entity.Notification;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.notification.repository.NotificationRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
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
    private final UserRepository userRepository;

    public void createNotification(User user, NotificationType type, String content) {

        notificationRepository.save(Notification.create(user, type, content));
    }

    public void createManyNotification(List<User> recipients, NotificationType type, String content) {
        for(User u : recipients){
            createNotification(u, type, content);
        }
    }

    // 읽음 처리
    @Transactional
    public void updateReadAt(Long NotificationId) {
        Notification notification = notificationRepository.findById(NotificationId)
                .orElseThrow(() -> new BusinessException("읽음 실패 - 존재하지 않는 알림 입니다."));

        notification.updateReadAt();
    }

    // 알림 조회(전체)
    @Transactional(readOnly = true)
    public NotificationListResponse getAllUserNotifications(Long userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Order.desc("createdAt")));

        Page<Notification> pageList = notificationRepository.findByRecipientId(userId, pageable);

        List<NotificationDetailResponse> list = pageList.getContent().stream()
                .map(NotificationDetailResponse::from)
                .toList();

        return NotificationListResponse.from(
                page,
                size,
                list,
                pageList.getTotalPages(),
                pageList.getTotalElements(),
                pageList.isFirst(),
                pageList.isLast()
        );
    }
}

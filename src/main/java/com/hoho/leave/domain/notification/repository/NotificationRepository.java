package com.hoho.leave.domain.notification.repository;

import com.hoho.leave.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 알림 리포지토리.
 * <p>
 * 알림 엔티티에 대한 데이터베이스 접근 기능을 제공한다.
 * </p>
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    /**
     * 특정 수신자의 알림을 페이지네이션하여 조회한다.
     *
     * @param recipientId 수신자 ID
     * @param pageable 페이징 정보
     * @return 페이지네이션된 알림 목록
     */
    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);
}

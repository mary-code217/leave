package com.hoho.leave.domain.notification.repository;

import com.hoho.leave.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByRecipientId(Long recipientId, Pageable pageable);
}

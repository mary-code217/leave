package com.hoho.leave.domain.handover.facade;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.service.HandoverRecipientService;
import com.hoho.leave.domain.handover.service.HandoverService;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.notification.service.NotificationService;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandoverFacade {

    HandoverService handoverService;
    HandoverRecipientService handoverRecipientService;
    UserService userService;
    AuditLogService auditLogService;
    NotificationService notificationService;

    @Transactional
    public void createHandover(HandoverCreateRequest request) {
        User author = userService.getUserEntity(request.getAuthorId());

        List<User> recipients = userService.getUserEntityList(request.getRecipientIds());

        HandoverNote handover = handoverService.createHandover(author, request.getTitle(), request.getContent());

        handoverRecipientService.createRecipients(recipients, handover);

        notificationService.createManyNotification(
                recipients,
                NotificationType.HANDOVER_ASSIGNED,
                "인수인계가 도착하였습니다."
        );

        auditLogService.createLog(
                Action.HANDOVER_CREATE,
                author.getId(),
                AuditObjectType.HANDOVER,
                handover.getId(),
                "["+author.getUsername()+"]님이 ["+getNames(recipients)+"]에게 인수인계를 남겼습니다."
        );
    }

    /**
     * 수신자 이름 조인 메서드
     */
    private String getNames(List<User> findRecipients) {
        return findRecipients.stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
    }
}

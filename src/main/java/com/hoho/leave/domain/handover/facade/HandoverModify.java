package com.hoho.leave.domain.handover.facade;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.handover.dto.request.HandoverUpdateRequest;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.service.HandoverRecipientService;
import com.hoho.leave.domain.handover.service.HandoverService;
import com.hoho.leave.domain.handover.service.support.RecipientChangeSet;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.notification.service.NotificationService;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 인수인계 생성, 수정, 삭제 퍼사드
 */
@Service
@RequiredArgsConstructor
public class HandoverModify {

    private final HandoverService handoverService;
    private final HandoverRecipientService handoverRecipientService;
    private final UserService userService;
    private final AuditLogService auditLogService;
    private final NotificationService notificationService;

    @Transactional
    public void createHandover(HandoverCreateRequest request) {
        User author = userService.getUserEntity(request.getAuthorId());

        List<User> recipients = userService.getUserEntityList(request.getRecipientIds());

        HandoverNote note = handoverService.createHandover(author, request.getTitle(), request.getContent());

        handoverRecipientService.createRecipients(recipients, note);

        sendNotification(recipients);

        createLog(author.getId(), note.getId(), author.getUsername(), getNames(recipients));
    }


    @Transactional
    public void deleteHandover(Long handoverId) {
        handoverService.deleteHandover(handoverId);

        handoverRecipientService.deleteByHandoverNoteId(handoverId);
    }

    @Transactional
    public void updateHandover(Long handoverId, HandoverUpdateRequest request) {
        HandoverNote note = handoverService.updateHandover(handoverId, request.getTitle(), request.getContent());

        List<HandoverRecipient> recipients = handoverRecipientService.findAllByHandoverNoteId(note.getId());

        RecipientChangeSet set = handoverRecipientService.addAndDeleteRecipients(recipients, request.getRecipientIds());

        List<User> newRecipients = userService.getUserEntityList(set.getToAdd().stream().toList());

        handoverRecipientService.createRecipients(newRecipients, note);

        handoverRecipientService.deleteByHandoverIdAndRecipientIds(handoverId, set.getToRemove());

        sendNotification(newRecipients);

        updateLog(note.getAuthor().getId(), note.getId(), note.getAuthor().getUsername());
    }

    /**
     * 수신자 이름 조인 메서드
     */
    private String getNames(List<User> findRecipients) {
        return findRecipients.stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
    }

    /**
     * 인수인계 알림 발송(수신자)
     */
    private void sendNotification(List<User> recipients) {
        notificationService.createManyNotification(
                recipients,
                NotificationType.HANDOVER_ASSIGNED,
                "인수인계가 도착하였습니다."
        );
    }

    /**
     * 인수인계 발송 로그 생성
     */
    private void createLog(Long authorId, Long handoverId, String authorName, String recipients) {
        auditLogService.createLog(
                Action.HANDOVER_CREATE,
                authorId,
                AuditObjectType.HANDOVER,
                handoverId,
                "[" + authorName + "]님이 [" + recipients + "]에게 인수인계를 남겼습니다."
        );
    }

    /**
     * 인수인계 수정 로그 생성
     */
    private void updateLog(Long authorId, Long handoverId, String authorName) {
        auditLogService.createLog(
                Action.HANDOVER_UPDATE,
                authorId,
                AuditObjectType.HANDOVER,
                handoverId,
                "[" + authorName + "]님이 인수인계를 수정하였습니다."
        );
    }
}

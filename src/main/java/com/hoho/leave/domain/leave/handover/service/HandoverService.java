package com.hoho.leave.domain.leave.handover.service;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.leave.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.leave.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.leave.handover.entity.HandoverNote;
import com.hoho.leave.domain.leave.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.leave.handover.repository.HandoverNoteRepository;
import com.hoho.leave.domain.leave.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HandoverService {

    private final HandoverNoteRepository handoverNoteRepository;
    private final HandoverRecipientRepository handoverRecipientRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Transactional
    public void createHandover(HandoverCreateRequest dto) {
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new BusinessException("작성 실패 - 존재하지 않는 유저 입니다."));

        HandoverNote saveNote = handoverNoteRepository.save(HandoverNote.create(author, dto.getTitle(), dto.getContent()));

        for(Long recipientId : dto.getRecipientIds()) {
            User recipient = userRepository.findById(recipientId)
                    .orElseThrow(() -> new BusinessException("발신 실패 - 존재하지 않는 수신자 입니다."));

            handoverRecipientRepository.save(HandoverRecipient.create(saveNote, recipient));

            auditLogService.createLog(
                    Action.HANDOVER_CREATE,
                    author.getId(),
                    AuditObjectType.HANDOVER,
                    saveNote.getId(),
                    author.getUsername()+"이 "+recipient.getUsername()+"에게 인수인계를 남겼습니다."
            );
        }
    }

    @Transactional(readOnly = true)
    public HandoverAuthorListResponse getHandoverAuthorList(Long authorId, Integer page, Integer size) {

        return new HandoverAuthorListResponse();
    }
}

package com.hoho.leave.domain.leave.handover.service;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.leave.handover.dto.request.HandoverCreateRequest;
import com.hoho.leave.domain.leave.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.leave.handover.dto.response.HandoverAuthorResponse;
import com.hoho.leave.domain.leave.handover.entity.HandoverNote;
import com.hoho.leave.domain.leave.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.leave.handover.repository.HandoverNoteRepository;
import com.hoho.leave.domain.leave.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import com.hoho.leave.util.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<User> findRecipients = userRepository.findAllById(dto.getRecipientIds());

        List<HandoverRecipient> handoverRecipients = findRecipients.stream()
                .map(u -> HandoverRecipient.create(saveNote, u))
                .toList();
        handoverRecipientRepository.saveAll(handoverRecipients);

        auditLogService.createLog(
                Action.HANDOVER_CREATE,
                author.getId(),
                AuditObjectType.HANDOVER,
                saveNote.getId(),
                "["+author.getUsername()+"]님이 ["+getNames(findRecipients)+"]에게 인수인계를 남겼습니다."
        );
    }

    // 수신자 이름 조인 메서드
    private String getNames(List<User> findRecipients) {
        return findRecipients.stream()
                .map(User::getUsername)
                .collect(Collectors.joining(", "));
    }

    // 발신목록
    @Transactional(readOnly = true)
    public HandoverAuthorListResponse getHandoverAuthorList(Long authorId, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<HandoverNote> pageList = handoverNoteRepository.findByAuthorId(authorId, pageable);

        List<HandoverAuthorResponse> list = new ArrayList<>();
        for(HandoverNote note : pageList.getContent()) {
            List<HandoverRecipient> recipients = handoverRecipientRepository.findAllByHandoverNoteId(note.getId());

            List<String> recipientNames = new ArrayList<>();
            for(HandoverRecipient recipient : recipients) {
                recipientNames.add(recipient.getRecipient().getUsername());
            }

            HandoverAuthorResponse response = HandoverAuthorResponse.from(
                    note.getId(),
                    note.getAuthor().getUsername(),
                    recipientNames,
                    note.getTitle(),
                    note.getContent(),
                    note.getCreatedAt()
            );
            list.add(response);
        }

        return HandoverAuthorListResponse.from(
                page,
                size,
                list,
                pageList.getTotalPages(),
                pageList.getTotalElements(),
                pageList.isFirst(),
                pageList.isLast()
        );
    }

    // 수신목록
}
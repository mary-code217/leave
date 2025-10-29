package com.hoho.leave.domain.handover.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
import com.hoho.leave.domain.handover.dto.request.HandoverUpdateRequest;
import com.hoho.leave.domain.handover.dto.response.*;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.repository.HandoverNoteRepository;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandoverService {

    private final HandoverNoteRepository handoverNoteRepository;
    private final HandoverRecipientRepository handoverRecipientRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    public HandoverNote createHandover(User author, String title, String content) {
        return handoverNoteRepository.save(HandoverNote.create(author, title, content));
    }

    // 발신목록
    @Transactional(readOnly = true)
    public HandoverAuthorListResponse getHandoverAuthorList(Long authorId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<HandoverNote> pageList = handoverNoteRepository.findByAuthorId(authorId, pageable);

        List<Long> noteIds = pageList.getContent().stream()
                .map(HandoverNote::getId)
                .toList();

        List<HandoverRecipientRepository.RecipientUsernameRow> rows =
                handoverRecipientRepository.findRecipientUsernamesByNoteIds(noteIds);

        Map<Long, List<String>> recipientsByNoteId = rows.stream()
                .collect(Collectors.groupingBy(
                        HandoverRecipientRepository.RecipientUsernameRow::getNoteId,
                        Collectors.mapping(HandoverRecipientRepository.RecipientUsernameRow::getUsername, Collectors.toList())
                ));

        List<HandoverAuthorResponse> list = pageList.getContent().stream()
                .map(note -> HandoverAuthorResponse.from(
                        note.getId(),
                        note.getAuthor().getUsername(),
                        recipientsByNoteId.getOrDefault(note.getId(), List.of()),
                        note.getTitle(),
                        note.getContent(),
                        note.getCreatedAt()
                ))
                .toList();

        return HandoverAuthorListResponse.of(
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
    @Transactional(readOnly = true)
    public HandoverRecipientListResponse getRecipientList(Long recipientId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<HandoverRecipient> pageList = handoverRecipientRepository.findByRecipientId(recipientId, pageable);

        List<HandoverRecipientResponse> list = pageList.getContent().stream()
                .map(h -> HandoverRecipientResponse.of(h.getHandoverNote())).toList();

        return HandoverRecipientListResponse.of(
                page,
                size,
                list,
                pageList.getTotalPages(),
                pageList.getTotalElements(),
                pageList.isFirst(),
                pageList.isLast()
        );
    }

    // 단건조회
    @Transactional(readOnly = true)
    public HandoverDetailResponse getHandover(Long handoverId) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);

        List<String> recipients = handoverRecipientRepository.findRecipientNamesByHandoverNoteId(handoverNote.getId());

        return HandoverDetailResponse.of(
                handoverNote.getId(),
                handoverNote.getAuthor().getUsername(),
                recipients,
                handoverNote.getTitle(),
                handoverNote.getContent(),
                handoverNote.getCreatedAt()
        );
    }

    public HandoverNote updateHandover(Long handoverId, String title, String content) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        handoverNote.update(title, content);

        return handoverNote;
    }

    public void deleteHandover(Long handoverId) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        handoverNoteRepository.delete(handoverNote);
    }

    /**
     * 인수인계 엔티티 조회
     */
    private HandoverNote getHandoverNoteEntity(Long handoverId) {
        return handoverNoteRepository.findById(handoverId)
                .orElseThrow(() -> new NotFoundException("Not Found HandoverNote : " + handoverId));
    }


}
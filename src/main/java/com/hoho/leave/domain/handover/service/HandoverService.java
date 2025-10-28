package com.hoho.leave.domain.handover.service;

import com.hoho.leave.common.exception.BusinessException;
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
    @Transactional(readOnly = true)
    public HandoverRecipientListResponse getRecipientList(Long recipientId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
        Page<HandoverRecipient> pageList = handoverRecipientRepository.findByRecipientId(recipientId, pageable);

        List<HandoverRecipientResponse> list = pageList.getContent().stream()
                .map(h -> {
                    return HandoverRecipientResponse.from(h.getHandoverNote());
                }).toList();


        return HandoverRecipientListResponse.from(
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
        HandoverNote handoverNote = handoverNoteRepository.findByIdWithAuthor(handoverId)
                .orElseThrow(() -> new BusinessException("조회 실패 - 존재하지 않는 인수인계 입니다."));

        List<String> recipients = handoverRecipientRepository.findRecipientNamesByHandoverNoteId(handoverNote.getId());

        return HandoverDetailResponse.from(
                handoverNote.getId(),
                handoverNote.getAuthor().getUsername(),
                recipients,
                handoverNote.getTitle(),
                handoverNote.getContent(),
                handoverNote.getCreatedAt()
        );
    }

    // 인수인계 수정
    @Transactional
    public void updateHandover(Long handoverId, HandoverUpdateRequest dto) {
        HandoverNote handoverNote = handoverNoteRepository.findByIdWithAuthor(handoverId)
                .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 인수인계 입니다."));

        handoverNote.update(dto.getTitle(), dto.getContent());

        List<HandoverRecipient> recipients = handoverRecipientRepository.findAllByHandoverNoteId(handoverNote.getId());

        Set<Long> existingIds = recipients.stream()
                .map(hr -> hr.getRecipient().getId())
                .collect(Collectors.toSet());
        Set<Long> desiredIds = new HashSet<>(dto.getRecipientIds());

        Set<Long> toAdd = new HashSet<>(desiredIds);
        toAdd.removeAll(existingIds);
        Set<Long> toRemove = new HashSet<>(existingIds);
        toRemove.removeAll(desiredIds);

        List<HandoverRecipient> newRecipients = toAdd.stream()
                .map(u -> HandoverRecipient.create(handoverNote, userRepository.findById(u)
                        .orElseThrow(() -> new BusinessException("수정 실패 - 존재하지 않는 사용자 입니다."))))
                .toList();
        handoverRecipientRepository.saveAll(newRecipients);

        handoverRecipientRepository.deleteByNoteIdAndRecipientIds(handoverNote.getId(), toRemove);

        auditLogService.createLog(
                Action.HANDOVER_UPDATE,
                handoverNote.getAuthor().getId(),
                AuditObjectType.HANDOVER,
                handoverNote.getId(),
                "[" + handoverNote.getAuthor().getUsername() + "]님이 인수인계를 수정하였습니다."
        );
    }

    //삭제
    @Transactional
    public void deleteHandover(Long handoverId) {
        HandoverNote handoverNote = handoverNoteRepository.findById(handoverId)
                .orElseThrow(() -> new BusinessException("삭제 실패 - 존재하지 않는 인수인계 입니다."));

        handoverRecipientRepository.deleteByHandoverNoteId(handoverNote.getId());
        handoverNoteRepository.delete(handoverNote);
    }


}
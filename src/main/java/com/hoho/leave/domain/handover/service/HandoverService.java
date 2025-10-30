package com.hoho.leave.domain.handover.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverAuthorResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverDetailResponse;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.repository.HandoverNoteRepository;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository.RecipientUsernameRow;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 인수인계 서비스
 */
@Service
@RequiredArgsConstructor
public class HandoverService {

    private final HandoverNoteRepository noteRepository;
    private final HandoverRecipientRepository recipientRepository;

    public HandoverNote createHandover(User author, String title, String content) {
        return noteRepository.save(HandoverNote.create(author, title, content));
    }

    public HandoverNote updateHandover(Long handoverId, String title, String content) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        handoverNote.update(title, content);

        return handoverNote;
    }

    public void deleteHandover(Long handoverId) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        noteRepository.delete(handoverNote);
    }

    // 발신목록
    @Transactional(readOnly = true)
    public HandoverAuthorListResponse getHandoverAuthorList(Long authorId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<HandoverNote> pageList = noteRepository.findByAuthorId(authorId, pageable);

        List<Long> noteIds = pageList.getContent().stream().map(HandoverNote::getId).toList();

        List<RecipientUsernameRow> rows = recipientRepository.findRecipientUsernamesByNoteIds(noteIds);

        List<HandoverAuthorResponse> list = getHandoverAuthorResponses(rows, pageList);

        return HandoverAuthorListResponse.of(pageList, list);
    }

    // 단건조회
    @Transactional(readOnly = true)
    public HandoverDetailResponse getHandover(Long handoverId) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        List<String> recipients = recipientRepository.findRecipientNamesByHandoverNoteId(handoverNote.getId());

        return HandoverDetailResponse.of(handoverNote, recipients);
    }

    /**
     * 인수인계 엔티티 조회
     */
    private HandoverNote getHandoverNoteEntity(Long handoverId) {
        return noteRepository.findById(handoverId)
                .orElseThrow(() -> new NotFoundException("Not Found HandoverNote : " + handoverId));
    }
    
    /**
     * 페이지 객체 생성
     */
    private PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
    }

    /**
     * 페이지 리스트 -> 발신목록 응답 리스트 변환
     */
    private List<HandoverAuthorResponse> getHandoverAuthorResponses(List<RecipientUsernameRow> rows, Page<HandoverNote> pageList) {
        Map<Long, List<String>> recipientsByNoteId = getLongListMap(rows);

        return pageList.getContent().stream()
                .map(note -> HandoverAuthorResponse.of(
                        note, recipientsByNoteId.getOrDefault(note.getId(), List.of())
                )).toList();
    }

    /**
     * Map<Long, List<String>> 변환
     */
    private Map<Long, List<String>> getLongListMap(List<RecipientUsernameRow> rows) {
        return rows.stream()
                .collect(Collectors.groupingBy(
                        RecipientUsernameRow::getNoteId,
                        Collectors.mapping(RecipientUsernameRow::getUsername, Collectors.toList())
                ));
    }
}
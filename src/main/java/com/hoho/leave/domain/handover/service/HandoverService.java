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
 * 인수인계 서비스.
 * <p>
 * 인수인계 노트의 생성, 수정, 삭제 및 조회 기능을 제공한다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class HandoverService {

    private final HandoverNoteRepository noteRepository;
    private final HandoverRecipientRepository recipientRepository;

    /**
     * 인수인계 노트를 생성한다.
     *
     * @param author  작성자
     * @param title   제목
     * @param content 내용
     * @return 생성된 인수인계 노트
     */
    public HandoverNote createHandover(User author, String title, String content) {
        return noteRepository.save(HandoverNote.create(author, title, content));
    }

    /**
     * 인수인계 노트를 수정한다.
     *
     * @param handoverId 인수인계 ID
     * @param title      새 제목
     * @param content    새 내용
     * @return 수정된 인수인계 노트
     */
    public HandoverNote updateHandover(Long handoverId, String title, String content) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        handoverNote.update(title, content);

        return handoverNote;
    }

    /**
     * 인수인계 노트를 삭제한다.
     *
     * @param handoverId 삭제할 인수인계 ID
     */
    public void deleteHandover(Long handoverId) {
        HandoverNote handoverNote = getHandoverNoteEntity(handoverId);
        noteRepository.delete(handoverNote);
    }

    /**
     * 작성자의 발신 인수인계 목록을 조회한다.
     *
     * @param authorId 작성자 ID
     * @param page     페이지 번호
     * @param size     페이지 크기
     * @return 발신 인수인계 목록 응답
     */
    @Transactional(readOnly = true)
    public HandoverAuthorListResponse getHandoverAuthorList(Long authorId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<HandoverNote> pageList = noteRepository.findByAuthorId(authorId, pageable);

        List<Long> noteIds = pageList.getContent().stream().map(HandoverNote::getId).toList();

        List<RecipientUsernameRow> rows = recipientRepository.findRecipientUsernamesByNoteIds(noteIds);

        List<HandoverAuthorResponse> list = getHandoverAuthorResponses(rows, pageList);

        return HandoverAuthorListResponse.of(pageList, list);
    }

    /**
     * 인수인계를 단건 조회한다.
     *
     * @param handoverId 인수인계 ID
     * @return 인수인계 상세 응답
     */
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
package com.hoho.leave.domain.handover.service;

import com.hoho.leave.domain.handover.dto.response.HandoverRecipientListResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverRecipientResponse;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.handover.service.support.RecipientChangeSet;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 인수인계 수신자 서비스
 */
@Service
@RequiredArgsConstructor
public class HandoverRecipientService {

    private final HandoverRecipientRepository repository;

    /**
     * 수신자 생성
     */
    public List<HandoverRecipient> createRecipients(List<User> recipients, HandoverNote handover) {
        return repository.saveAll(
                recipients.stream()
                        .map(u -> HandoverRecipient.create(handover, u))
                        .toList()
        );
    }

    // 수신목록
    @Transactional(readOnly = true)
    public HandoverRecipientListResponse getRecipientList(Long recipientId, Integer page, Integer size) {
        Pageable pageable = getPageable(page, size);
        Page<HandoverRecipient> pageList = repository.findByRecipientId(recipientId, pageable);

        List<HandoverRecipientResponse> list = getRecipientResponseList(pageList);

        return HandoverRecipientListResponse.of(pageList, list);
    }

    /**
     * 해당 인수인계의 수신자 전체 삭제
     */
    public void deleteByHandoverNoteId(Long HandoverNoteId) {
        repository.deleteByHandoverNoteId(HandoverNoteId);
    }

    /**
     * 해당 인수인계의 해당 수신자 삭제
     */
    public void deleteByHandoverIdAndRecipientIds(Long handoverId, Collection<Long> recipientIds) {
        repository.deleteByNoteIdAndRecipientIds(handoverId, recipientIds);
    }

    /**
     * 해당 인수인계 수신자 전체 조회
     */
    public List<HandoverRecipient> findAllByHandoverNoteId(Long handoverNoteId) {
        return repository.findAllByHandoverNoteId(handoverNoteId);
    }

    /**
     * 인수인계 수정시 추가/삭제 대상 수신자 도출
     */
    public RecipientChangeSet addAndDeleteRecipients(List<HandoverRecipient> recipients, List<Long> newRecipientIds) {
        Set<Long> existingIds = recipients.stream()
                .map(hr -> hr.getRecipient().getId())
                .collect(Collectors.toSet());
        Set<Long> desiredIds = new HashSet<>(newRecipientIds);

        return RecipientChangeSet.of(existingIds, desiredIds);
    }

    /**
     * 페이지 객체 생성
     */
    private PageRequest getPageable(Integer page, Integer size) {
        return PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("createdAt")));
    }

    /**
     * 페이지 리스트 -> 수신목록 응답 리스트 변환
     */
    private List<HandoverRecipientResponse> getRecipientResponseList(Page<HandoverRecipient> pageList) {
        return pageList.getContent().stream()
                .map(h -> HandoverRecipientResponse.of(h.getHandoverNote())).toList();
    }
}
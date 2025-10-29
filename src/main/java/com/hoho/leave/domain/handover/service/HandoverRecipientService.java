package com.hoho.leave.domain.handover.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.domain.handover.dto.Diff;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HandoverRecipientService {

    HandoverRecipientRepository repository;

    public List<HandoverRecipient> createRecipients(List<User> recipients, HandoverNote handover) {
        return repository.saveAll(
                recipients.stream()
                        .map(u -> HandoverRecipient.create(handover, u))
                        .toList()
        );
    }

    public void deleteByHandoverNoteId(Long HandoverNoteId) {
        repository.deleteByHandoverNoteId(HandoverNoteId);
    }

    public void deleteByHandoverIdAndRecipientIds(Long handoverId, Collection<Long> recipientIds) {
        repository.deleteByNoteIdAndRecipientIds(handoverId, recipientIds);
    }

    public List<HandoverRecipient> findAllByHandoverNoteId(Long handoverNoteId) {
        return repository.findAllByHandoverNoteId(handoverNoteId);
    }

    public Diff addAndDeleteRecipients(List<HandoverRecipient> recipients, List<Long> newRecipientIds) {
        Set<Long> existingIds = recipients.stream()
                .map(hr -> hr.getRecipient().getId())
                .collect(Collectors.toSet());
        Set<Long> desiredIds = new HashSet<>(newRecipientIds);

        return Diff.setting(existingIds, desiredIds);
    }

    /**
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
     */
}
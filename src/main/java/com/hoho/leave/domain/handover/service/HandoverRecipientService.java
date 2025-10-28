package com.hoho.leave.domain.handover.service;

import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
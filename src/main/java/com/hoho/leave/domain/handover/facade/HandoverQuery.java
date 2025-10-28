package com.hoho.leave.domain.handover.facade;

import com.hoho.leave.domain.handover.service.HandoverRecipientService;
import com.hoho.leave.domain.handover.service.HandoverService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 인수인계 조회 퍼사드
 */
@Service
@RequiredArgsConstructor
public class HandoverQuery {

    private final HandoverService handoverService;
    private final HandoverRecipientService recipientService;


}

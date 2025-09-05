package com.hoho.leave.domain.leave.handover.repository;

import com.hoho.leave.domain.leave.handover.entity.HandoverRecipient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverRecipientRepository extends JpaRepository<HandoverRecipient, Long> {
}

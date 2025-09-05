package com.hoho.leave.domain.leave.handover.repository;

import com.hoho.leave.domain.leave.handover.entity.HandoverNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverNoteRepository extends JpaRepository<HandoverNote, Long> {
}

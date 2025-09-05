package com.hoho.leave.domain.leave.handover.repository;

import com.hoho.leave.domain.leave.handover.entity.HandoverNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HandoverNoteRepository extends JpaRepository<HandoverNote, Long> {

    Page<HandoverNote> findByAuthorId(Long authorId, Pageable pageable);


}

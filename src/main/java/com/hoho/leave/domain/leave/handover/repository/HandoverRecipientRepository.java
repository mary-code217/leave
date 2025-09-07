package com.hoho.leave.domain.leave.handover.repository;

import com.hoho.leave.domain.leave.handover.entity.HandoverRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface HandoverRecipientRepository extends JpaRepository<HandoverRecipient, Long> {

    @Query("""
        select hr.handoverNote.id as noteId,
               hr.recipient.username as username
        from HandoverRecipient hr
        where hr.handoverNote.id in :noteIds
        """)
    List<RecipientUsernameRow> findRecipientUsernamesByNoteIds(@Param("noteIds") Collection<Long> noteIds);

    interface RecipientUsernameRow {
        Long getNoteId();
        String getUsername();
    }

    @EntityGraph(attributePaths = {"handoverNote", "handoverNote.author", "recipient"})
    Page<HandoverRecipient> findByRecipientId(Long recipientId, Pageable pageable);

    List<HandoverRecipient> findAllByHandoverNoteId(Long handoverNoteId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        delete from HandoverRecipient hr
        where hr.handoverNote.id = :noteId
          and hr.recipient.id in :recipientIds
        """)
    void deleteByNoteIdAndRecipientIds(@Param("noteId") Long noteId,
                                       @Param("recipientIds") Collection<Long> recipientIds);
}

package com.hoho.leave.domain.leave.handover.repository;

import com.hoho.leave.domain.leave.handover.entity.HandoverNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface HandoverNoteRepository extends JpaRepository<HandoverNote, Long> {
    @EntityGraph(attributePaths = {"author"})
    Page<HandoverNote> findByAuthorId(Long authorId, Pageable pageable);

    @Query("""
    select n from HandoverNote n
    join fetch n.author
    where n.id = :id
    """)
    Optional<HandoverNote> findByIdWithAuthor(@Param("id") Long id);

}

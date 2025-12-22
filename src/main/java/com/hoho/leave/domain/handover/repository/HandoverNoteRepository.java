package com.hoho.leave.domain.handover.repository;

import com.hoho.leave.domain.handover.entity.HandoverNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * 인수인계 노트 데이터 접근 레포지토리.
 */
public interface HandoverNoteRepository extends JpaRepository<HandoverNote, Long> {

    /**
     * 작성자 ID로 인수인계 목록을 조회한다.
     *
     * @param authorId 작성자 ID
     * @param pageable 페이징 정보
     * @return 인수인계 노트 페이지
     */
    @EntityGraph(attributePaths = {"author"})
    Page<HandoverNote> findByAuthorId(Long authorId, Pageable pageable);

    /**
     * 인수인계를 작성자와 함께 조회한다.
     *
     * @param id 인수인계 ID
     * @return 작성자 정보가 포함된 인수인계 노트
     */
    @Query("""
    select n from HandoverNote n
    join fetch n.author
    where n.id = :id
    """)
    Optional<HandoverNote> findByIdWithAuthor(@Param("id") Long id);

}

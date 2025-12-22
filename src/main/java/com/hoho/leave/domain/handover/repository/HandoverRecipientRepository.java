package com.hoho.leave.domain.handover.repository;

import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

/**
 * 인수인계 수신자 데이터 접근 레포지토리.
 */
public interface HandoverRecipientRepository extends JpaRepository<HandoverRecipient, Long> {

    /**
     * 수신자 사용자명 조회를 위한 프로젝션 인터페이스.
     */
    interface RecipientUsernameRow {
        Long getNoteId();
        String getUsername();
    }

    /**
     * 인수인계 노트 ID 목록으로 수신자 사용자명을 조회한다.
     *
     * @param noteIds 인수인계 노트 ID 목록
     * @return 수신자 사용자명 행 목록
     */
    @Query("""
            select hr.handoverNote.id as noteId,
                   hr.recipient.username as username
            from HandoverRecipient hr
            where hr.handoverNote.id in :noteIds
            """)
    List<RecipientUsernameRow> findRecipientUsernamesByNoteIds(@Param("noteIds") Collection<Long> noteIds);

    /**
     * 수신자 ID로 수신한 인수인계 목록을 조회한다.
     *
     * @param recipientId 수신자 ID
     * @param pageable    페이징 정보
     * @return 인수인계 수신자 페이지
     */
    @EntityGraph(attributePaths = {"handoverNote", "handoverNote.author", "recipient"})
    Page<HandoverRecipient> findByRecipientId(Long recipientId, Pageable pageable);

    /**
     * 인수인계 노트 ID로 모든 수신자를 조회한다.
     *
     * @param handoverNoteId 인수인계 노트 ID
     * @return 수신자 목록
     */
    @EntityGraph(attributePaths = "recipient", type = EntityGraph.EntityGraphType.FETCH)
    List<HandoverRecipient> findAllByHandoverNoteId(Long handoverNoteId);

    /**
     * 인수인계 노트와 수신자 ID로 수신자를 삭제한다.
     *
     * @param noteId       인수인계 노트 ID
     * @param recipientIds 삭제할 수신자 ID 목록
     */
    @Modifying
    @Query("""
            delete from HandoverRecipient hr
            where hr.handoverNote.id = :noteId
              and hr.recipient.id in :recipientIds
            """)
    void deleteByNoteIdAndRecipientIds(@Param("noteId") Long noteId,
                                       @Param("recipientIds") Collection<Long> recipientIds);

    /**
     * 인수인계 노트의 모든 수신자를 삭제한다.
     *
     * @param handoverNoteId 인수인계 노트 ID
     */
    void deleteByHandoverNoteId(Long handoverNoteId);

    /**
     * 인수인계 노트의 수신자 이름 목록을 조회한다.
     *
     * @param handoverNoteId 인수인계 노트 ID
     * @return 수신자 이름 목록
     */
    @Query("""
            select u.username
            from HandoverRecipient r
            join r.recipient u
            where r.handoverNote.id = :handoverNoteId
            order by r.id
            """)
    List<String> findRecipientNamesByHandoverNoteId(Long handoverNoteId);
}

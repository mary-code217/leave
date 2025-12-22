package com.hoho.leave.domain.leave.request.repository;

import com.hoho.leave.domain.leave.request.entity.LeaveRequestAttachment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 첨부파일 리포지토리.
 * <p>
 * 휴가 신청 첨부파일의 데이터 접근을 담당한다.
 * </p>
 */
public interface AttachmentRepository extends JpaRepository<LeaveRequestAttachment, Long> {
    /**
     * 특정 휴가 신청의 첨부파일 목록을 조회한다.
     *
     * @param leaveRequestId 휴가 신청 ID
     * @return 첨부파일 목록
     */
    @EntityGraph(attributePaths = {"uploadedBy"})
    List<LeaveRequestAttachment> findByLeaveRequestId(Long leaveRequestId);

    /**
     * 여러 휴가 신청의 첨부파일 개수를 집계한다.
     *
     * @param ids 휴가 신청 ID 목록
     * @return 휴가 신청별 첨부파일 개수
     */
    @Query("""
       select a.leaveRequest.id as reqId, count(a.id) as cnt
       from LeaveRequestAttachment a
       where a.leaveRequest.id in :ids
       group by a.leaveRequest.id
    """)
    List<ReqIdCount> countByLeaveRequestIds(@Param("ids") List<Long> ids);

    /**
     * 휴가 신청 ID와 첨부파일 개수를 담는 프로젝션 인터페이스.
     */
    interface ReqIdCount {
        /**
         * 휴가 신청 ID를 반환한다.
         *
         * @return 휴가 신청 ID
         */
        Long getReqId();

        /**
         * 첨부파일 개수를 반환한다.
         *
         * @return 첨부파일 개수
         */
        long getCnt();
    }
}

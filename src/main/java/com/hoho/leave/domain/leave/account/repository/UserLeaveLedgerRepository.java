package com.hoho.leave.domain.leave.account.repository;

import com.hoho.leave.domain.leave.account.entity.UserLeaveLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 사용자 휴가 원장 리포지토리.
 * 
 * 휴가 원장 엔티티에 대한 데이터베이스 접근을 제공한다.
 * 
 */
public interface UserLeaveLedgerRepository extends JpaRepository<UserLeaveLedger, Long> {
    /**
     * 휴가 원장 목록을 페이징하여 조회한다.
     * 사용자 정보를 함께 조회하여 N+1 문제를 방지한다.
     *
     * @param pageable 페이징 정보
     * @return 휴가 원장 페이지
     */
    @EntityGraph(attributePaths = {"userLeaves", "userLeaves.user"})
    Page<UserLeaveLedger> findAll(Pageable pageable);
}

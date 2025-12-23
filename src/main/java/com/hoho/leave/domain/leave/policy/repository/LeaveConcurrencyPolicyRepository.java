package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 휴가 동시 제한 정책 리포지토리.
 * 
 * 휴가 동시 제한 정책 엔티티에 대한 데이터베이스 접근을 담당한다.
 * 
 */
public interface LeaveConcurrencyPolicyRepository extends JpaRepository<LeaveConcurrencyPolicy, Long> {

    /**
     * 중복되는 정책이 존재하는지 확인한다.
     *
     * @param teamId 팀 ID
     * @param leaveTypeId 휴가 유형 ID
     * @param newFrom 새 정책 시작일
     * @param newTo 새 정책 종료일 (null = 무기한)
     * @return 중복 존재 여부
     */
    @Query("""
            select (count(p) > 0)
            from LeaveConcurrencyPolicy p
            where p.team.id = :teamId
              and p.leaveType.id = :leaveTypeId
              and ( (:newTo is null or p.effectiveFrom < :newTo)
                    and (p.effectiveTo is null or :newFrom < p.effectiveTo) )
            """)
    boolean existsOverlappingHalfOpen(
            @Param("teamId") Long teamId,
            @Param("leaveTypeId") Long leaveTypeId,
            @Param("newFrom") LocalDate newFrom,
            @Param("newTo") LocalDate newTo // null = 오픈엔드
    );

    /**
     * 특정 기간을 포함하는 정책 목록을 조회한다.
     *
     * @param teamId 팀 ID
     * @param from 시작일
     * @param to 종료일
     * @return 해당 기간을 포함하는 정책 목록
     */
    @Query("""
            select p
              from LeaveConcurrencyPolicy p
             where p.team.id = :teamId
               and p.effectiveFrom <= :from
               and (p.effectiveTo is null or p.effectiveTo >= :to)
             order by p.effectiveFrom desc
            """)
    List<LeaveConcurrencyPolicy> findCoveringRange(
            @Param("teamId") Long teamId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );

    /**
     * 특정 기간을 포함하는 정책이 존재하는지 확인한다.
     *
     * @param teamId 팀 ID
     * @param from 시작일
     * @param to 종료일
     * @return 존재 여부
     */
    @Query("""
            select (count(p) > 0)
              from LeaveConcurrencyPolicy p
             where p.team.id = :teamId
               and p.effectiveFrom <= :from
               and (p.effectiveTo is null or p.effectiveTo >= :to)
            """)
    boolean existsCoveringRange(
            @Param("teamId") Long teamId,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to
    );
}

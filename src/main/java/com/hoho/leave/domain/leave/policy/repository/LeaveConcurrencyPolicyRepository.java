package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.LeaveConcurrencyPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface LeaveConcurrencyPolicyRepository extends JpaRepository<LeaveConcurrencyPolicy, Long> {
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
}

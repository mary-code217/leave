package com.hoho.leave.domain.leave.policy.repository;

import com.hoho.leave.domain.leave.policy.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 휴가 유형 리포지토리.
 * 
 * 휴가 유형 엔티티에 대한 데이터베이스 접근을 담당한다.
 * 
 */
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Long> {
    /**
     * 휴가 유형명으로 존재 여부를 확인한다.
     *
     * @param leaveName 휴가 유형명
     * @return 존재 여부
     */
    boolean existsByLeaveName(String leaveName);
}

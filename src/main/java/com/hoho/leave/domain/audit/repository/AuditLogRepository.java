package com.hoho.leave.domain.audit.repository;

import com.hoho.leave.domain.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 감사 로그 데이터 접근 레포지토리.
 */
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    /**
     * 객체 유형별로 감사 로그를 페이징하여 조회한다.
     *
     * @param objectType 대상 객체 유형
     * @param pageable   페이징 정보
     * @return 감사 로그 페이지
     */
    Page<AuditLog> findAllByObjectType(String objectType, Pageable pageable);

}

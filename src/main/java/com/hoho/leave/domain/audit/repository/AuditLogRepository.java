package com.hoho.leave.domain.audit.repository;

import com.hoho.leave.domain.audit.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findAllByObjectType(String objectType, Pageable pageable);

}

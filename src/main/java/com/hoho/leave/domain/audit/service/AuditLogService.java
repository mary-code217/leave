package com.hoho.leave.domain.audit.service;

import com.hoho.leave.domain.audit.repository.AuditLogRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;



}

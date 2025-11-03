package com.hoho.leave.domain.audit.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.audit.dto.response.AuditLogDetailResponse;
import com.hoho.leave.domain.audit.dto.response.AuditLogListResponse;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.audit.repository.AuditLogRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public void createLog(Action action, Long actorId,
                          String objectType, Long objectId,
                          String summary) {

        AuditLog auditLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);

        auditLogRepository.save(auditLog);
    }

    @Transactional(readOnly = true)
    public AuditLogListResponse getAllLogs(Integer page, Integer size, String objectType) {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<AuditLog> pageList =
                objectType.equals("all") ?
                        auditLogRepository.findAll(pageable) :
                        auditLogRepository.findAllByObjectType(objectType, pageable);

        List<AuditLogDetailResponse> list = new ArrayList<>();
        for (AuditLog auditLog : pageList.getContent()) {
            if (auditLog.getActorId() != null) {
                User user = userRepository.findById(auditLog.getActorId())
                        .orElseThrow(() -> new NotFoundException("Not Found User : " + auditLog.getActorId()));
                list.add(AuditLogDetailResponse.of(auditLog, user));
            } else {
                list.add(AuditLogDetailResponse.of(auditLog, null));
            }
        }

        return AuditLogListResponse.of(pageList, list);
    }
}

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

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /**
     * 감사 로그 목록 조회 (N+1 최적화)
     * - 기존: 로그 N개 조회 시 User N번 = N+1 쿼리
     * - 개선: 로그 목록 1번 + User IN 쿼리 1번 = 2 쿼리
     */
    @Transactional(readOnly = true)
    public AuditLogListResponse getAllLogs(Integer page, Integer size, String objectType) {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<AuditLog> pageList =
                objectType.equals("all") ?
                        auditLogRepository.findAll(pageable) :
                        auditLogRepository.findAllByObjectType(objectType, pageable);

        // 로그에서 actorId 추출 (null 제외, 중복 제거)
        List<Long> actorIds = pageList.getContent().stream()
                .map(AuditLog::getActorId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // User 일괄 조회 후 Map으로 변환 (IN 쿼리 1회)
        Map<Long, User> userMap = userRepository.findAllById(actorIds).stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));

        // Map에서 User 조회하여 응답 DTO 생성
        List<AuditLogDetailResponse> list = pageList.getContent().stream()
                .map(log -> AuditLogDetailResponse.of(log, userMap.get(log.getActorId())))
                .toList();

        return AuditLogListResponse.of(pageList, list);
    }
}

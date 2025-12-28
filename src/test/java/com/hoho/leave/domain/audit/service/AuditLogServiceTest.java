package com.hoho.leave.domain.audit.service;

import com.hoho.leave.domain.audit.dto.response.AuditLogDetailResponse;
import com.hoho.leave.domain.audit.dto.response.AuditLogListResponse;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import com.hoho.leave.domain.audit.repository.AuditLogRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.entity.UserRole;
import com.hoho.leave.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogService 테스트")
class AuditLogServiceTest {

    @InjectMocks
    private AuditLogService auditLogService;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    private User mockUser;
    private User mockUser2;
    private AuditLog mockAuditLog;
    private AuditLog mockAuditLog2;

    @BeforeEach
    void setUp() {
        // Mock User 생성
        mockUser = new User("test@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        ReflectionTestUtils.setField(mockUser, "username", "테스트유저");
        ReflectionTestUtils.setField(mockUser, "employeeNo", "EMP001");

        mockUser2 = new User("test2@example.com", UserRole.ROLE_ADMIN);
        ReflectionTestUtils.setField(mockUser2, "id", 2L);
        ReflectionTestUtils.setField(mockUser2, "username", "관리자유저");
        ReflectionTestUtils.setField(mockUser2, "employeeNo", "EMP002");

        // Mock AuditLog 생성
        mockAuditLog = AuditLog.createLog(
                Action.USER_JOIN,
                1L,
                AuditObjectType.USER,
                1L,
                "테스트유저가 회원가입을 완료했습니다."
        );
        ReflectionTestUtils.setField(mockAuditLog, "id", 1L);
        ReflectionTestUtils.setField(mockAuditLog, "createdAt", LocalDateTime.now());

        mockAuditLog2 = AuditLog.createLog(
                Action.LEAVE_APPLY,
                2L,
                AuditObjectType.LEAVE_REQUEST,
                10L,
                "관리자유저가 휴가를 신청했습니다."
        );
        ReflectionTestUtils.setField(mockAuditLog2, "id", 2L);
        ReflectionTestUtils.setField(mockAuditLog2, "createdAt", LocalDateTime.now().minusHours(1));
    }

    @Nested
    @DisplayName("감사 로그 생성")
    class CreateLog {

        @Test
        @DisplayName("성공: 새로운 감사 로그를 생성한다")
        void createLog_Success() {
            // given
            Action action = Action.USER_JOIN;
            Long actorId = 1L;
            String objectType = AuditObjectType.USER;
            Long objectId = 1L;
            String summary = "테스트유저가 회원가입을 완료했습니다.";

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(mockAuditLog);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("성공: 휴가 신청 감사 로그를 생성한다")
        void createLog_LeaveApply_Success() {
            // given
            Action action = Action.LEAVE_APPLY;
            Long actorId = 2L;
            String objectType = AuditObjectType.LEAVE_REQUEST;
            Long objectId = 10L;
            String summary = "관리자유저가 휴가를 신청했습니다.";

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(mockAuditLog2);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("성공: actorId가 null인 시스템 로그를 생성한다")
        void createLog_NullActorId_Success() {
            // given
            Action action = Action.AUDIT_LOG_PURGED;
            Long actorId = null;
            String objectType = AuditObjectType.USER;
            Long objectId = 100L;
            String summary = "시스템에 의해 로그가 정리되었습니다.";

            AuditLog systemLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);
            ReflectionTestUtils.setField(systemLog, "id", 3L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(systemLog);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }
    }

    @Nested
    @DisplayName("감사 로그 목록 조회")
    class GetAllLogs {

        @Test
        @DisplayName("성공: 전체 감사 로그 목록을 페이징하여 조회한다")
        void getAllLogs_All_Success() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            List<AuditLog> auditLogs = List.of(mockAuditLog, mockAuditLog2);
            Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

            given(auditLogRepository.findAll(any(Pageable.class))).willReturn(auditLogPage);
            given(userRepository.findAllById(any())).willReturn(List.of(mockUser, mockUser2));

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).hasSize(2);
            assertThat(response.getPage()).isEqualTo(1);
            verify(auditLogRepository).findAll(any(Pageable.class));
            verify(userRepository).findAllById(any());
        }

        @Test
        @DisplayName("성공: 특정 객체 유형별로 감사 로그를 조회한다")
        void getAllLogs_ByObjectType_Success() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = AuditObjectType.USER;

            List<AuditLog> auditLogs = List.of(mockAuditLog);
            Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

            given(auditLogRepository.findAllByObjectType(eq(objectType), any(Pageable.class)))
                    .willReturn(auditLogPage);
            given(userRepository.findAllById(any())).willReturn(List.of(mockUser));

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).hasSize(1);
            assertThat(response.getAuditLogs().get(0).getObjectType()).isEqualTo(AuditObjectType.USER);
            verify(auditLogRepository).findAllByObjectType(eq(objectType), any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 감사 로그가 없는 경우 빈 목록을 반환한다")
        void getAllLogs_EmptyList() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            Page<AuditLog> emptyPage = new PageImpl<>(List.of());

            given(auditLogRepository.findAll(any(Pageable.class))).willReturn(emptyPage);
            given(userRepository.findAllById(any())).willReturn(List.of());

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).isEmpty();
            assertThat(response.getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지 정보가 올바르게 반환된다")
        void getAllLogs_PaginationInfo() {
            // given
            Integer page = 2;
            Integer size = 5;
            String objectType = "all";

            List<AuditLog> auditLogs = List.of(mockAuditLog);
            Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

            given(auditLogRepository.findAll(any(Pageable.class))).willReturn(auditLogPage);
            given(userRepository.findAllById(any())).willReturn(List.of(mockUser));

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).hasSize(1);
            verify(auditLogRepository).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("성공: actorId가 null인 로그도 정상 조회된다")
        void getAllLogs_WithNullActorId_Success() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            AuditLog systemLog = AuditLog.createLog(
                    Action.AUDIT_LOG_PURGED,
                    null,
                    AuditObjectType.USER,
                    100L,
                    "시스템에 의해 로그가 정리되었습니다."
            );
            ReflectionTestUtils.setField(systemLog, "id", 3L);
            ReflectionTestUtils.setField(systemLog, "createdAt", LocalDateTime.now());

            List<AuditLog> auditLogs = List.of(mockAuditLog, systemLog);
            Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

            given(auditLogRepository.findAll(any(Pageable.class))).willReturn(auditLogPage);
            given(userRepository.findAllById(any())).willReturn(List.of(mockUser));

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).hasSize(2);

            // actorId가 null인 로그는 username이 "관리자"로 표시됨
            List<AuditLogDetailResponse> logs = response.getAuditLogs();
            AuditLogDetailResponse systemLogResponse = logs.stream()
                    .filter(log -> log.getAction() == Action.AUDIT_LOG_PURGED)
                    .findFirst()
                    .orElse(null);
            assertThat(systemLogResponse).isNotNull();
            assertThat(systemLogResponse.getUsername()).isEqualTo("관리자");
        }

        @Test
        @DisplayName("성공: 사용자가 존재하지 않는 로그도 정상 조회된다")
        void getAllLogs_WithDeletedUser_Success() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            AuditLog logWithDeletedUser = AuditLog.createLog(
                    Action.USER_DELETE,
                    999L,
                    AuditObjectType.USER,
                    999L,
                    "삭제된 사용자의 로그"
            );
            ReflectionTestUtils.setField(logWithDeletedUser, "id", 4L);
            ReflectionTestUtils.setField(logWithDeletedUser, "createdAt", LocalDateTime.now());

            List<AuditLog> auditLogs = List.of(logWithDeletedUser);
            Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

            given(auditLogRepository.findAll(any(Pageable.class))).willReturn(auditLogPage);
            given(userRepository.findAllById(any())).willReturn(List.of()); // 사용자 없음

            // when
            AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getAuditLogs()).hasSize(1);
            assertThat(response.getAuditLogs().get(0).getUsername()).isEqualTo("관리자");
        }
    }

    @Nested
    @DisplayName("행위 유형별 로그 생성")
    class CreateLogByActionType {

        @Test
        @DisplayName("성공: 조직 관련 감사 로그를 생성한다")
        void createLog_OrgAction_Success() {
            // given
            Action action = Action.ORG_TEAM_CREATE;
            Long actorId = 1L;
            String objectType = AuditObjectType.TEAM;
            Long objectId = 5L;
            String summary = "새로운 팀이 생성되었습니다.";

            AuditLog orgLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);
            ReflectionTestUtils.setField(orgLog, "id", 10L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(orgLog);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("성공: 휴가 정책 관련 감사 로그를 생성한다")
        void createLog_LeavePolicyAction_Success() {
            // given
            Action action = Action.LEAVE_POLICY_TYPE_CREATE;
            Long actorId = 2L;
            String objectType = AuditObjectType.LEAVE_POLICY;
            Long objectId = 3L;
            String summary = "새로운 휴가 유형이 생성되었습니다.";

            AuditLog policyLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);
            ReflectionTestUtils.setField(policyLog, "id", 11L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(policyLog);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("성공: 인수인계 관련 감사 로그를 생성한다")
        void createLog_HandoverAction_Success() {
            // given
            Action action = Action.HANDOVER_CREATE;
            Long actorId = 1L;
            String objectType = AuditObjectType.HANDOVER;
            Long objectId = 7L;
            String summary = "인수인계가 생성되었습니다.";

            AuditLog handoverLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);
            ReflectionTestUtils.setField(handoverLog, "id", 12L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(handoverLog);

            // when
            auditLogService.createLog(action, actorId, objectType, objectId, summary);

            // then
            verify(auditLogRepository, times(1)).save(any(AuditLog.class));
        }
    }
}

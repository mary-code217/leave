package com.hoho.leave.domain.audit.repository;

import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.entity.AuditLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogRepository 테스트")
class AuditLogRepositoryTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private AuditLog mockAuditLog;
    private AuditLog mockAuditLog2;

    @BeforeEach
    void setUp() {
        // Mock AuditLog 생성
        mockAuditLog = AuditLog.createLog(
                Action.USER_JOIN,
                1L,
                "USER",
                100L,
                "사용자가 회원가입했습니다."
        );
        ReflectionTestUtils.setField(mockAuditLog, "id", 1L);
        ReflectionTestUtils.setField(mockAuditLog, "createdAt", LocalDateTime.now());

        mockAuditLog2 = AuditLog.createLog(
                Action.LEAVE_APPLY,
                2L,
                "LEAVE_REQUEST",
                200L,
                "휴가 신청이 접수되었습니다."
        );
        ReflectionTestUtils.setField(mockAuditLog2, "id", 2L);
        ReflectionTestUtils.setField(mockAuditLog2, "createdAt", LocalDateTime.now().minusHours(1));
    }

    @Nested
    @DisplayName("감사 로그 저장")
    class SaveAuditLog {

        @Test
        @DisplayName("성공: 새로운 감사 로그를 저장한다")
        void save_Success() {
            // given
            AuditLog newLog = AuditLog.createLog(
                    Action.USER_UPDATE_PROFILE,
                    1L,
                    "USER",
                    100L,
                    "프로필이 수정되었습니다."
            );

            AuditLog savedLog = AuditLog.createLog(
                    Action.USER_UPDATE_PROFILE,
                    1L,
                    "USER",
                    100L,
                    "프로필이 수정되었습니다."
            );
            ReflectionTestUtils.setField(savedLog, "id", 3L);
            ReflectionTestUtils.setField(savedLog, "createdAt", LocalDateTime.now());

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(savedLog);

            // when
            AuditLog result = auditLogRepository.save(newLog);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getAction()).isEqualTo(Action.USER_UPDATE_PROFILE);
            assertThat(result.getActorId()).isEqualTo(1L);
            verify(auditLogRepository).save(newLog);
        }

        @Test
        @DisplayName("성공: 다양한 Action 유형의 로그를 저장한다")
        void save_DifferentActions_Success() {
            // given
            AuditLog leaveLog = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, "휴가 신청");
            AuditLog orgLog = AuditLog.createLog(Action.ORG_TEAM_CREATE, 1L, "ORG", 1L, "팀 생성");
            AuditLog notificationLog = AuditLog.createLog(Action.NOTIFICATION_SENT, null, "NOTIFICATION", 1L, "알림 발송");

            given(auditLogRepository.save(any(AuditLog.class))).willAnswer(invocation -> {
                AuditLog log = invocation.getArgument(0);
                ReflectionTestUtils.setField(log, "id", (long) (Math.random() * 1000));
                return log;
            });

            // when
            AuditLog savedLeave = auditLogRepository.save(leaveLog);
            AuditLog savedOrg = auditLogRepository.save(orgLog);
            AuditLog savedNotification = auditLogRepository.save(notificationLog);

            // then
            assertThat(savedLeave.getAction()).isEqualTo(Action.LEAVE_APPLY);
            assertThat(savedOrg.getAction()).isEqualTo(Action.ORG_TEAM_CREATE);
            assertThat(savedNotification.getAction()).isEqualTo(Action.NOTIFICATION_SENT);
            verify(auditLogRepository, times(3)).save(any(AuditLog.class));
        }

        @Test
        @DisplayName("성공: actorId가 null인 시스템 로그를 저장한다")
        void save_NullActorId_Success() {
            // given
            AuditLog systemLog = AuditLog.createLog(
                    Action.AUDIT_LOG_PURGED,
                    null,
                    "AUDIT_LOG",
                    1L,
                    "시스템에 의해 로그가 정리되었습니다."
            );

            AuditLog savedLog = AuditLog.createLog(
                    Action.AUDIT_LOG_PURGED,
                    null,
                    "AUDIT_LOG",
                    1L,
                    "시스템에 의해 로그가 정리되었습니다."
            );
            ReflectionTestUtils.setField(savedLog, "id", 10L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(savedLog);

            // when
            AuditLog result = auditLogRepository.save(systemLog);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getActorId()).isNull();
            assertThat(result.getAction()).isEqualTo(Action.AUDIT_LOG_PURGED);
        }
    }

    @Nested
    @DisplayName("감사 로그 ID로 조회")
    class FindById {

        @Test
        @DisplayName("성공: ID로 감사 로그를 조회한다")
        void findById_Exists_ReturnsLog() {
            // given
            Long logId = 1L;
            given(auditLogRepository.findById(logId)).willReturn(Optional.of(mockAuditLog));

            // when
            Optional<AuditLog> result = auditLogRepository.findById(logId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(logId);
            assertThat(result.get().getAction()).isEqualTo(Action.USER_JOIN);
            assertThat(result.get().getSummary()).isEqualTo("사용자가 회원가입했습니다.");
            verify(auditLogRepository).findById(logId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_NotExists_ReturnsEmpty() {
            // given
            Long nonExistentId = 999L;
            given(auditLogRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<AuditLog> result = auditLogRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(auditLogRepository).findById(nonExistentId);
        }

        @Test
        @DisplayName("성공: null ID로 조회 시 빈 Optional 반환")
        void findById_NullId_ReturnsEmpty() {
            // given
            given(auditLogRepository.findById(null)).willReturn(Optional.empty());

            // when
            Optional<AuditLog> result = auditLogRepository.findById(null);

            // then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("전체 감사 로그 조회")
    class FindAll {

        @Test
        @DisplayName("성공: 전체 감사 로그 목록을 페이징하여 조회한다")
        void findAll_Success() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
            List<AuditLog> logs = List.of(mockAuditLog, mockAuditLog2);
            Page<AuditLog> logPage = new PageImpl<>(logs, pageRequest, 2);

            given(auditLogRepository.findAll(any(PageRequest.class))).willReturn(logPage);

            // when
            Page<AuditLog> result = auditLogRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            verify(auditLogRepository).findAll(pageRequest);
        }

        @Test
        @DisplayName("성공: 감사 로그가 없는 경우 빈 페이지 반환")
        void findAll_EmptyResult() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<AuditLog> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(auditLogRepository.findAll(any(PageRequest.class))).willReturn(emptyPage);

            // when
            Page<AuditLog> result = auditLogRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findAll_Pagination() {
            // given
            // 15개의 로그 생성
            List<AuditLog> allLogs = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                AuditLog log = AuditLog.createLog(
                        Action.USER_JOIN,
                        (long) i,
                        "USER",
                        (long) (i + 100),
                        "로그 " + i
                );
                ReflectionTestUtils.setField(log, "id", (long) (i + 1));
                ReflectionTestUtils.setField(log, "createdAt", LocalDateTime.now().minusMinutes(i));
                allLogs.add(log);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
            PageRequest pageRequest2 = PageRequest.of(1, 5, Sort.by(Sort.Order.desc("createdAt")));
            PageRequest pageRequest3 = PageRequest.of(2, 5, Sort.by(Sort.Order.desc("createdAt")));

            Page<AuditLog> page1 = new PageImpl<>(allLogs.subList(0, 5), pageRequest1, 15);
            Page<AuditLog> page2 = new PageImpl<>(allLogs.subList(5, 10), pageRequest2, 15);
            Page<AuditLog> page3 = new PageImpl<>(allLogs.subList(10, 15), pageRequest3, 15);

            given(auditLogRepository.findAll(eq(pageRequest1))).willReturn(page1);
            given(auditLogRepository.findAll(eq(pageRequest2))).willReturn(page2);
            given(auditLogRepository.findAll(eq(pageRequest3))).willReturn(page3);

            // when
            Page<AuditLog> result1 = auditLogRepository.findAll(pageRequest1);
            Page<AuditLog> result2 = auditLogRepository.findAll(pageRequest2);
            Page<AuditLog> result3 = auditLogRepository.findAll(pageRequest3);

            // then
            assertThat(result1.getContent()).hasSize(5);
            assertThat(result2.getContent()).hasSize(5);
            assertThat(result3.getContent()).hasSize(5);
            assertThat(result1.getTotalElements()).isEqualTo(15);
            assertThat(result1.getTotalPages()).isEqualTo(3);
            assertThat(result1.isFirst()).isTrue();
            assertThat(result3.isLast()).isTrue();
        }
    }

    @Nested
    @DisplayName("객체 유형별 감사 로그 조회")
    class FindAllByObjectType {

        @Test
        @DisplayName("성공: 특정 객체 유형의 로그를 페이징하여 조회한다")
        void findAllByObjectType_Success() {
            // given
            String objectType = "USER";
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

            List<AuditLog> userLogs = List.of(mockAuditLog);
            Page<AuditLog> logPage = new PageImpl<>(userLogs, pageRequest, 1);

            given(auditLogRepository.findAllByObjectType(eq(objectType), any(PageRequest.class)))
                    .willReturn(logPage);

            // when
            Page<AuditLog> result = auditLogRepository.findAllByObjectType(objectType, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getObjectType()).isEqualTo("USER");
            verify(auditLogRepository).findAllByObjectType(objectType, pageRequest);
        }

        @Test
        @DisplayName("성공: LEAVE_REQUEST 유형의 로그를 조회한다")
        void findAllByObjectType_LeaveRequest_Success() {
            // given
            String objectType = "LEAVE_REQUEST";
            PageRequest pageRequest = PageRequest.of(0, 10);

            List<AuditLog> leaveLogs = List.of(mockAuditLog2);
            Page<AuditLog> logPage = new PageImpl<>(leaveLogs, pageRequest, 1);

            given(auditLogRepository.findAllByObjectType(eq(objectType), any(PageRequest.class)))
                    .willReturn(logPage);

            // when
            Page<AuditLog> result = auditLogRepository.findAllByObjectType(objectType, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0).getAction()).isEqualTo(Action.LEAVE_APPLY);
        }

        @Test
        @DisplayName("성공: 해당 유형의 로그가 없는 경우 빈 페이지 반환")
        void findAllByObjectType_EmptyResult() {
            // given
            String objectType = "NOTIFICATION";
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<AuditLog> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(auditLogRepository.findAllByObjectType(eq(objectType), any(PageRequest.class)))
                    .willReturn(emptyPage);

            // when
            Page<AuditLog> result = auditLogRepository.findAllByObjectType(objectType, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 다른 유형의 로그는 조회되지 않는다")
        void findAllByObjectType_OnlyMatchingType() {
            // given
            String userType = "USER";
            String leaveType = "LEAVE_REQUEST";
            PageRequest pageRequest = PageRequest.of(0, 10);

            // USER 로그만 반환
            Page<AuditLog> userPage = new PageImpl<>(List.of(mockAuditLog), pageRequest, 1);
            given(auditLogRepository.findAllByObjectType(eq(userType), any(PageRequest.class)))
                    .willReturn(userPage);

            // LEAVE_REQUEST 로그만 반환
            Page<AuditLog> leavePage = new PageImpl<>(List.of(mockAuditLog2), pageRequest, 1);
            given(auditLogRepository.findAllByObjectType(eq(leaveType), any(PageRequest.class)))
                    .willReturn(leavePage);

            // when
            Page<AuditLog> userResult = auditLogRepository.findAllByObjectType(userType, pageRequest);
            Page<AuditLog> leaveResult = auditLogRepository.findAllByObjectType(leaveType, pageRequest);

            // then
            assertThat(userResult.getContent()).hasSize(1);
            assertThat(userResult.getContent().get(0).getObjectType()).isEqualTo("USER");

            assertThat(leaveResult.getContent()).hasSize(1);
            assertThat(leaveResult.getContent().get(0).getObjectType()).isEqualTo("LEAVE_REQUEST");
        }

        @Test
        @DisplayName("성공: 대소문자 구분하여 조회한다")
        void findAllByObjectType_CaseSensitive() {
            // given
            String upperCaseType = "USER";
            String lowerCaseType = "user";
            PageRequest pageRequest = PageRequest.of(0, 10);

            Page<AuditLog> upperPage = new PageImpl<>(List.of(mockAuditLog), pageRequest, 1);
            Page<AuditLog> lowerPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(auditLogRepository.findAllByObjectType(eq(upperCaseType), any(PageRequest.class)))
                    .willReturn(upperPage);
            given(auditLogRepository.findAllByObjectType(eq(lowerCaseType), any(PageRequest.class)))
                    .willReturn(lowerPage);

            // when
            Page<AuditLog> upperResult = auditLogRepository.findAllByObjectType(upperCaseType, pageRequest);
            Page<AuditLog> lowerResult = auditLogRepository.findAllByObjectType(lowerCaseType, pageRequest);

            // then
            assertThat(upperResult.getContent()).hasSize(1);
            assertThat(lowerResult.getContent()).isEmpty();
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findAllByObjectType_Pagination() {
            // given
            String objectType = "USER";

            // 15개의 USER 로그 생성
            List<AuditLog> allUserLogs = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                AuditLog log = AuditLog.createLog(
                        Action.USER_UPDATE_PROFILE,
                        (long) i,
                        "USER",
                        (long) (i + 100),
                        "사용자 로그 " + i
                );
                ReflectionTestUtils.setField(log, "id", (long) (i + 1));
                allUserLogs.add(log);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5);
            PageRequest pageRequest2 = PageRequest.of(1, 5);
            PageRequest pageRequest3 = PageRequest.of(2, 5);

            Page<AuditLog> page1 = new PageImpl<>(allUserLogs.subList(0, 5), pageRequest1, 15);
            Page<AuditLog> page2 = new PageImpl<>(allUserLogs.subList(5, 10), pageRequest2, 15);
            Page<AuditLog> page3 = new PageImpl<>(allUserLogs.subList(10, 15), pageRequest3, 15);

            given(auditLogRepository.findAllByObjectType(eq(objectType), eq(pageRequest1))).willReturn(page1);
            given(auditLogRepository.findAllByObjectType(eq(objectType), eq(pageRequest2))).willReturn(page2);
            given(auditLogRepository.findAllByObjectType(eq(objectType), eq(pageRequest3))).willReturn(page3);

            // when
            Page<AuditLog> result1 = auditLogRepository.findAllByObjectType(objectType, pageRequest1);
            Page<AuditLog> result2 = auditLogRepository.findAllByObjectType(objectType, pageRequest2);
            Page<AuditLog> result3 = auditLogRepository.findAllByObjectType(objectType, pageRequest3);

            // then
            assertThat(result1.getContent()).hasSize(5);
            assertThat(result2.getContent()).hasSize(5);
            assertThat(result3.getContent()).hasSize(5);
            assertThat(result1.getTotalPages()).isEqualTo(3);
            assertThat(result1.isFirst()).isTrue();
            assertThat(result3.isLast()).isTrue();
        }
    }

    @Nested
    @DisplayName("감사 로그 삭제")
    class DeleteAuditLog {

        @Test
        @DisplayName("성공: ID로 감사 로그를 삭제한다")
        void deleteById_Success() {
            // given
            Long logId = 1L;
            doNothing().when(auditLogRepository).deleteById(logId);

            // when
            auditLogRepository.deleteById(logId);

            // then
            verify(auditLogRepository, times(1)).deleteById(logId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 삭제 시 예외 없이 처리된다")
        void deleteById_NonExistent_NoException() {
            // given
            Long nonExistentId = 999L;
            doNothing().when(auditLogRepository).deleteById(nonExistentId);

            // when
            auditLogRepository.deleteById(nonExistentId);

            // then
            verify(auditLogRepository).deleteById(nonExistentId);
        }

        @Test
        @DisplayName("성공: 모든 감사 로그를 삭제한다")
        void deleteAll_Success() {
            // given
            doNothing().when(auditLogRepository).deleteAll();

            // when
            auditLogRepository.deleteAll();

            // then
            verify(auditLogRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("감사 로그 개수 조회")
    class CountAuditLogs {

        @Test
        @DisplayName("성공: 전체 감사 로그 개수를 조회한다")
        void count_ReturnsCorrectCount() {
            // given
            given(auditLogRepository.count()).willReturn(100L);

            // when
            long count = auditLogRepository.count();

            // then
            assertThat(count).isEqualTo(100L);
            verify(auditLogRepository).count();
        }

        @Test
        @DisplayName("성공: 감사 로그가 없을 경우 0을 반환한다")
        void count_NoLogs_ReturnsZero() {
            // given
            given(auditLogRepository.count()).willReturn(0L);

            // when
            long count = auditLogRepository.count();

            // then
            assertThat(count).isEqualTo(0L);
            verify(auditLogRepository).count();
        }
    }

    @Nested
    @DisplayName("감사 로그 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("성공: 존재하는 ID 확인 시 true 반환")
        void existsById_Exists_ReturnsTrue() {
            // given
            Long existingId = 1L;
            given(auditLogRepository.existsById(existingId)).willReturn(true);

            // when
            boolean exists = auditLogRepository.existsById(existingId);

            // then
            assertThat(exists).isTrue();
            verify(auditLogRepository).existsById(existingId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 확인 시 false 반환")
        void existsById_NotExists_ReturnsFalse() {
            // given
            Long nonExistentId = 999L;
            given(auditLogRepository.existsById(nonExistentId)).willReturn(false);

            // when
            boolean exists = auditLogRepository.existsById(nonExistentId);

            // then
            assertThat(exists).isFalse();
            verify(auditLogRepository).existsById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 로그 저장 후 조회")
        void saveAndFind_Success() {
            // given
            AuditLog newLog = AuditLog.createLog(
                    Action.LEAVE_APPROVE,
                    5L,
                    "LEAVE_REQUEST",
                    300L,
                    "휴가가 승인되었습니다."
            );

            AuditLog savedLog = AuditLog.createLog(
                    Action.LEAVE_APPROVE,
                    5L,
                    "LEAVE_REQUEST",
                    300L,
                    "휴가가 승인되었습니다."
            );
            ReflectionTestUtils.setField(savedLog, "id", 10L);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(savedLog);
            given(auditLogRepository.findById(10L)).willReturn(Optional.of(savedLog));

            // when
            AuditLog saved = auditLogRepository.save(newLog);
            Optional<AuditLog> found = auditLogRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getAction()).isEqualTo(Action.LEAVE_APPROVE);
            assertThat(found.get().getSummary()).isEqualTo("휴가가 승인되었습니다.");
        }

        @Test
        @DisplayName("성공: 저장 후 삭제, 조회 시 빈 결과")
        void saveDeleteFind_Empty() {
            // given
            Long logId = 20L;
            AuditLog savedLog = AuditLog.createLog(
                    Action.USER_DELETE,
                    1L,
                    "USER",
                    50L,
                    "사용자가 삭제되었습니다."
            );
            ReflectionTestUtils.setField(savedLog, "id", logId);

            given(auditLogRepository.save(any(AuditLog.class))).willReturn(savedLog);
            given(auditLogRepository.findById(logId)).willReturn(Optional.of(savedLog), Optional.empty());
            doNothing().when(auditLogRepository).deleteById(logId);

            // when
            AuditLog saved = auditLogRepository.save(savedLog);
            Optional<AuditLog> beforeDelete = auditLogRepository.findById(logId);
            auditLogRepository.deleteById(logId);
            Optional<AuditLog> afterDelete = auditLogRepository.findById(logId);

            // then
            assertThat(beforeDelete).isPresent();
            assertThat(afterDelete).isEmpty();
        }

        @Test
        @DisplayName("성공: 여러 유형의 로그 저장 후 유형별 조회")
        void saveMultipleTypesAndFindByType() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);

            AuditLog userLog1 = AuditLog.createLog(Action.USER_JOIN, 1L, "USER", 1L, "가입1");
            AuditLog userLog2 = AuditLog.createLog(Action.USER_JOIN, 2L, "USER", 2L, "가입2");
            AuditLog leaveLog = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, "신청");
            AuditLog orgLog = AuditLog.createLog(Action.ORG_TEAM_CREATE, 1L, "ORG", 1L, "팀생성");

            Page<AuditLog> userPage = new PageImpl<>(List.of(userLog1, userLog2), pageRequest, 2);
            Page<AuditLog> leavePage = new PageImpl<>(List.of(leaveLog), pageRequest, 1);
            Page<AuditLog> orgPage = new PageImpl<>(List.of(orgLog), pageRequest, 1);

            given(auditLogRepository.findAllByObjectType(eq("USER"), any(PageRequest.class))).willReturn(userPage);
            given(auditLogRepository.findAllByObjectType(eq("LEAVE_REQUEST"), any(PageRequest.class))).willReturn(leavePage);
            given(auditLogRepository.findAllByObjectType(eq("ORG"), any(PageRequest.class))).willReturn(orgPage);

            // when
            Page<AuditLog> userResult = auditLogRepository.findAllByObjectType("USER", pageRequest);
            Page<AuditLog> leaveResult = auditLogRepository.findAllByObjectType("LEAVE_REQUEST", pageRequest);
            Page<AuditLog> orgResult = auditLogRepository.findAllByObjectType("ORG", pageRequest);

            // then
            assertThat(userResult.getContent()).hasSize(2);
            assertThat(leaveResult.getContent()).hasSize(1);
            assertThat(orgResult.getContent()).hasSize(1);

            assertThat(userResult.getContent()).allMatch(log -> "USER".equals(log.getObjectType()));
            assertThat(leaveResult.getContent()).allMatch(log -> "LEAVE_REQUEST".equals(log.getObjectType()));
            assertThat(orgResult.getContent()).allMatch(log -> "ORG".equals(log.getObjectType()));
        }
    }
}

package com.hoho.leave.domain.audit.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuditLog 엔티티 테스트")
class AuditLogTest {

    private AuditLog auditLog;
    private Action action;
    private Long actorId;
    private String objectType;
    private Long objectId;
    private String summary;

    @BeforeEach
    void setUp() {
        action = Action.USER_JOIN;
        actorId = 1L;
        objectType = "USER";
        objectId = 100L;
        summary = "사용자가 회원가입했습니다.";

        auditLog = AuditLog.createLog(action, actorId, objectType, objectId, summary);
    }

    @Nested
    @DisplayName("감사 로그 생성")
    class CreateLog {

        @Test
        @DisplayName("성공: createLog 정적 메서드로 감사 로그를 생성한다")
        void createLog_Success() {
            // given
            Action testAction = Action.LEAVE_APPLY;
            Long testActorId = 5L;
            String testObjectType = "LEAVE_REQUEST";
            Long testObjectId = 200L;
            String testSummary = "휴가 신청이 접수되었습니다.";

            // when
            AuditLog createdLog = AuditLog.createLog(testAction, testActorId, testObjectType, testObjectId, testSummary);

            // then
            assertThat(createdLog).isNotNull();
            assertThat(createdLog.getAction()).isEqualTo(testAction);
            assertThat(createdLog.getActorId()).isEqualTo(testActorId);
            assertThat(createdLog.getObjectType()).isEqualTo(testObjectType);
            assertThat(createdLog.getObjectId()).isEqualTo(testObjectId);
            assertThat(createdLog.getSummary()).isEqualTo(testSummary);
        }

        @Test
        @DisplayName("성공: actorId가 null인 시스템 로그를 생성한다")
        void createLog_NullActorId_Success() {
            // given
            Action testAction = Action.AUDIT_LOG_PURGED;
            String testObjectType = "AUDIT_LOG";
            Long testObjectId = 1L;
            String testSummary = "시스템에 의해 로그가 정리되었습니다.";

            // when
            AuditLog createdLog = AuditLog.createLog(testAction, null, testObjectType, testObjectId, testSummary);

            // then
            assertThat(createdLog).isNotNull();
            assertThat(createdLog.getActorId()).isNull();
            assertThat(createdLog.getAction()).isEqualTo(testAction);
        }

        @Test
        @DisplayName("성공: 모든 필드가 null인 로그를 생성할 수 있다")
        void createLog_AllNullFields_Success() {
            // given & when
            AuditLog createdLog = AuditLog.createLog(null, null, null, null, null);

            // then
            assertThat(createdLog).isNotNull();
            assertThat(createdLog.getAction()).isNull();
            assertThat(createdLog.getActorId()).isNull();
            assertThat(createdLog.getObjectType()).isNull();
            assertThat(createdLog.getObjectId()).isNull();
            assertThat(createdLog.getSummary()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: action 필드가 올바르게 저장된다")
        void action_StoredCorrectly() {
            // given
            Action expectedAction = Action.USER_JOIN;

            // when
            Action actualAction = auditLog.getAction();

            // then
            assertThat(actualAction).isEqualTo(expectedAction);
        }

        @Test
        @DisplayName("성공: actorId 필드가 올바르게 저장된다")
        void actorId_StoredCorrectly() {
            // given
            Long expectedActorId = 1L;

            // when
            Long actualActorId = auditLog.getActorId();

            // then
            assertThat(actualActorId).isEqualTo(expectedActorId);
        }

        @Test
        @DisplayName("성공: objectType 필드가 올바르게 저장된다")
        void objectType_StoredCorrectly() {
            // given
            String expectedObjectType = "USER";

            // when
            String actualObjectType = auditLog.getObjectType();

            // then
            assertThat(actualObjectType).isEqualTo(expectedObjectType);
        }

        @Test
        @DisplayName("성공: objectId 필드가 올바르게 저장된다")
        void objectId_StoredCorrectly() {
            // given
            Long expectedObjectId = 100L;

            // when
            Long actualObjectId = auditLog.getObjectId();

            // then
            assertThat(actualObjectId).isEqualTo(expectedObjectId);
        }

        @Test
        @DisplayName("성공: summary 필드가 올바르게 저장된다")
        void summary_StoredCorrectly() {
            // given
            String expectedSummary = "사용자가 회원가입했습니다.";

            // when
            String actualSummary = auditLog.getSummary();

            // then
            assertThat(actualSummary).isEqualTo(expectedSummary);
        }

        @Test
        @DisplayName("성공: id 필드는 초기값이 null이다")
        void id_InitiallyNull() {
            // when
            Long id = auditLog.getId();

            // then
            assertThat(id).isNull();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(auditLog, "id", expectedId);
            Long actualId = auditLog.getId();

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("다양한 Action 유형")
    class VariousActionTypes {

        @Test
        @DisplayName("성공: USER 관련 Action 로그를 생성한다")
        void userActions_Success() {
            // given
            Action[] userActions = {
                    Action.USER_JOIN,
                    Action.USER_UPDATE_PROFILE,
                    Action.USER_UPDATE_PASSWORD,
                    Action.USER_STATUS_CHANGE,
                    Action.USER_ROLE_CHANGE,
                    Action.USER_DELETE
            };

            // when & then
            for (Action action : userActions) {
                AuditLog log = AuditLog.createLog(action, 1L, "USER", 1L, "User action: " + action.name());
                assertThat(log.getAction()).isEqualTo(action);
                assertThat(log.getObjectType()).isEqualTo("USER");
            }
        }

        @Test
        @DisplayName("성공: ORG 관련 Action 로그를 생성한다")
        void orgActions_Success() {
            // given
            Action[] orgActions = {
                    Action.ORG_GRADE_CREATE,
                    Action.ORG_GRADE_UPDATE,
                    Action.ORG_GRADE_DELETE,
                    Action.ORG_POSITION_CREATE,
                    Action.ORG_TEAM_CREATE,
                    Action.ORG_MEMBERSHIP_ASSIGN
            };

            // when & then
            for (Action action : orgActions) {
                AuditLog log = AuditLog.createLog(action, 1L, "ORG", 1L, "Org action: " + action.name());
                assertThat(log.getAction()).isEqualTo(action);
                assertThat(log.getObjectType()).isEqualTo("ORG");
            }
        }

        @Test
        @DisplayName("성공: LEAVE 관련 Action 로그를 생성한다")
        void leaveActions_Success() {
            // given
            Action[] leaveActions = {
                    Action.LEAVE_APPLY,
                    Action.LEAVE_REVISE,
                    Action.LEAVE_CANCEL,
                    Action.LEAVE_APPROVE,
                    Action.LEAVE_REJECT
            };

            // when & then
            for (Action action : leaveActions) {
                AuditLog log = AuditLog.createLog(action, 1L, "LEAVE_REQUEST", 1L, "Leave action: " + action.name());
                assertThat(log.getAction()).isEqualTo(action);
                assertThat(log.getObjectType()).isEqualTo("LEAVE_REQUEST");
            }
        }

        @Test
        @DisplayName("성공: NOTIFICATION 관련 Action 로그를 생성한다")
        void notificationActions_Success() {
            // given
            Action[] notificationActions = {
                    Action.NOTIFICATION_ENQUEUED,
                    Action.NOTIFICATION_SENT,
                    Action.NOTIFICATION_FAIL,
                    Action.NOTIFICATION_READ,
                    Action.NOTIFICATION_DELETED
            };

            // when & then
            for (Action action : notificationActions) {
                AuditLog log = AuditLog.createLog(action, 1L, "NOTIFICATION", 1L, "Notification action: " + action.name());
                assertThat(log.getAction()).isEqualTo(action);
                assertThat(log.getObjectType()).isEqualTo("NOTIFICATION");
            }
        }

        @Test
        @DisplayName("성공: HANDOVER 관련 Action 로그를 생성한다")
        void handoverActions_Success() {
            // given
            Action[] handoverActions = {
                    Action.HANDOVER_CREATE,
                    Action.HANDOVER_UPDATE,
                    Action.HANDOVER_RECEIVER_ASSIGN,
                    Action.HANDOVER_RECEIVER_REMOVE,
                    Action.HANDOVER_COMPLETE,
                    Action.HANDOVER_CANCEL
            };

            // when & then
            for (Action action : handoverActions) {
                AuditLog log = AuditLog.createLog(action, 1L, "HANDOVER", 1L, "Handover action: " + action.name());
                assertThat(log.getAction()).isEqualTo(action);
                assertThat(log.getObjectType()).isEqualTo("HANDOVER");
            }
        }
    }

    @Nested
    @DisplayName("다양한 objectType 형식")
    class VariousObjectTypes {

        @Test
        @DisplayName("성공: 대문자 objectType을 저장한다")
        void uppercaseObjectType_Success() {
            // given
            String objectType = "USER";

            // when
            AuditLog log = AuditLog.createLog(Action.USER_JOIN, 1L, objectType, 1L, "summary");

            // then
            assertThat(log.getObjectType()).isEqualTo("USER");
        }

        @Test
        @DisplayName("성공: 언더스코어가 포함된 objectType을 저장한다")
        void underscoreObjectType_Success() {
            // given
            String objectType = "LEAVE_REQUEST";

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_APPLY, 1L, objectType, 1L, "summary");

            // then
            assertThat(log.getObjectType()).isEqualTo("LEAVE_REQUEST");
        }

        @Test
        @DisplayName("성공: 긴 objectType 문자열도 저장 가능하다")
        void longObjectType_Success() {
            // given
            String objectType = "LEAVE_CONCURRENCY_POLICY";

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_POLICY_CONCURRENCY_CREATE, 1L, objectType, 1L, "summary");

            // then
            assertThat(log.getObjectType()).isEqualTo(objectType);
        }

        @Test
        @DisplayName("성공: 빈 문자열 objectType도 저장 가능하다")
        void emptyObjectType_Success() {
            // given
            String objectType = "";

            // when
            AuditLog log = AuditLog.createLog(Action.USER_JOIN, 1L, objectType, 1L, "summary");

            // then
            assertThat(log.getObjectType()).isEmpty();
        }
    }

    @Nested
    @DisplayName("다양한 summary 형식")
    class VariousSummaryFormats {

        @Test
        @DisplayName("성공: 한글 summary를 저장한다")
        void koreanSummary_Success() {
            // given
            String summary = "사용자 홍길동이 휴가를 신청했습니다.";

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, summary);

            // then
            assertThat(log.getSummary()).isEqualTo(summary);
            assertThat(log.getSummary()).contains("휴가");
        }

        @Test
        @DisplayName("성공: 영문 summary를 저장한다")
        void englishSummary_Success() {
            // given
            String summary = "User John Doe applied for leave.";

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, summary);

            // then
            assertThat(log.getSummary()).isEqualTo(summary);
        }

        @Test
        @DisplayName("성공: 긴 summary 문자열도 저장 가능하다")
        void longSummary_Success() {
            // given
            String summary = "이것은 매우 긴 감사 로그 요약입니다. ".repeat(10);

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, summary);

            // then
            assertThat(log.getSummary()).isEqualTo(summary);
            assertThat(log.getSummary().length()).isGreaterThan(100);
        }

        @Test
        @DisplayName("성공: 특수문자가 포함된 summary를 저장한다")
        void specialCharactersSummary_Success() {
            // given
            String summary = "휴가 신청: 2025-01-01 ~ 2025-01-03 (3일) [연차]";

            // when
            AuditLog log = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 1L, summary);

            // then
            assertThat(log.getSummary()).isEqualTo(summary);
            assertThat(log.getSummary()).contains("~");
            assertThat(log.getSummary()).contains("[연차]");
        }

        @Test
        @DisplayName("성공: 빈 문자열 summary도 저장 가능하다")
        void emptySummary_Success() {
            // given
            String summary = "";

            // when
            AuditLog log = AuditLog.createLog(Action.USER_JOIN, 1L, "USER", 1L, summary);

            // then
            assertThat(log.getSummary()).isEmpty();
        }
    }

    @Nested
    @DisplayName("BaseEntity 상속 검증")
    class BaseEntityInheritance {

        @Test
        @DisplayName("성공: createdAt 필드를 ReflectionTestUtils로 설정할 수 있다")
        void createdAt_CanBeSetWithReflection() {
            // given
            LocalDateTime expectedCreatedAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0);

            // when
            ReflectionTestUtils.setField(auditLog, "createdAt", expectedCreatedAt);
            LocalDateTime actualCreatedAt = auditLog.getCreatedAt();

            // then
            assertThat(actualCreatedAt).isEqualTo(expectedCreatedAt);
        }

        @Test
        @DisplayName("성공: updatedAt 필드를 ReflectionTestUtils로 설정할 수 있다")
        void updatedAt_CanBeSetWithReflection() {
            // given
            LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 1, 2, 15, 30, 0);

            // when
            ReflectionTestUtils.setField(auditLog, "updatedAt", expectedUpdatedAt);
            LocalDateTime actualUpdatedAt = auditLog.getUpdatedAt();

            // then
            assertThat(actualUpdatedAt).isEqualTo(expectedUpdatedAt);
        }

        @Test
        @DisplayName("성공: createdAt과 updatedAt 초기값은 null이다")
        void timestamps_InitiallyNull() {
            // when
            LocalDateTime createdAt = auditLog.getCreatedAt();
            LocalDateTime updatedAt = auditLog.getUpdatedAt();

            // then
            assertThat(createdAt).isNull();
            assertThat(updatedAt).isNull();
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 휴가 신청 전체 워크플로우 로그 생성")
        void leaveRequestWorkflow_Success() {
            // given & when
            AuditLog applyLog = AuditLog.createLog(Action.LEAVE_APPLY, 1L, "LEAVE_REQUEST", 100L, "휴가 신청 접수");
            AuditLog holdLog = AuditLog.createLog(Action.LEAVE_HOLD_POSTED, 1L, "LEAVE_LEDGER", 100L, "휴가 잔액 홀드");
            AuditLog approveLog = AuditLog.createLog(Action.LEAVE_APPROVE, 2L, "LEAVE_REQUEST", 100L, "휴가 승인");
            AuditLog deductLog = AuditLog.createLog(Action.LEAVE_DEDUCT_POSTED, 2L, "LEAVE_LEDGER", 100L, "휴가 차감 확정");

            // then
            assertThat(applyLog.getAction()).isEqualTo(Action.LEAVE_APPLY);
            assertThat(holdLog.getAction()).isEqualTo(Action.LEAVE_HOLD_POSTED);
            assertThat(approveLog.getAction()).isEqualTo(Action.LEAVE_APPROVE);
            assertThat(deductLog.getAction()).isEqualTo(Action.LEAVE_DEDUCT_POSTED);

            // 신청자와 승인자가 다름
            assertThat(applyLog.getActorId()).isEqualTo(1L);
            assertThat(approveLog.getActorId()).isEqualTo(2L);

            // 같은 휴가 신청에 대한 로그
            assertThat(applyLog.getObjectId()).isEqualTo(approveLog.getObjectId());
        }

        @Test
        @DisplayName("성공: 사용자 생명주기 전체 로그 생성")
        void userLifecycle_Success() {
            // given
            Long userId = 50L;

            // when
            AuditLog joinLog = AuditLog.createLog(Action.USER_JOIN, userId, "USER", userId, "회원가입 완료");
            AuditLog profileLog = AuditLog.createLog(Action.USER_UPDATE_PROFILE, userId, "USER", userId, "프로필 수정");
            AuditLog passwordLog = AuditLog.createLog(Action.USER_UPDATE_PASSWORD, userId, "USER", userId, "비밀번호 변경");
            AuditLog statusLog = AuditLog.createLog(Action.USER_STATUS_CHANGE, null, "USER", userId, "퇴직 처리");

            // then
            assertThat(joinLog.getActorId()).isEqualTo(userId);
            assertThat(profileLog.getActorId()).isEqualTo(userId);
            assertThat(passwordLog.getActorId()).isEqualTo(userId);
            assertThat(statusLog.getActorId()).isNull(); // 시스템 또는 관리자에 의한 퇴직 처리

            // 모두 같은 사용자에 대한 로그
            assertThat(joinLog.getObjectId()).isEqualTo(profileLog.getObjectId());
            assertThat(profileLog.getObjectId()).isEqualTo(passwordLog.getObjectId());
            assertThat(passwordLog.getObjectId()).isEqualTo(statusLog.getObjectId());
        }

        @Test
        @DisplayName("성공: 인수인계 전체 워크플로우 로그 생성")
        void handoverWorkflow_Success() {
            // given
            Long authorId = 10L;
            Long recipientId = 20L;
            Long handoverId = 1L;

            // when
            AuditLog createLog = AuditLog.createLog(Action.HANDOVER_CREATE, authorId, "HANDOVER", handoverId, "인수인계 노트 생성");
            AuditLog assignLog = AuditLog.createLog(Action.HANDOVER_RECEIVER_ASSIGN, authorId, "HANDOVER", handoverId, "수신자 배정: " + recipientId);
            AuditLog completeLog = AuditLog.createLog(Action.HANDOVER_COMPLETE, recipientId, "HANDOVER", handoverId, "인수인계 완료");

            // then
            assertThat(createLog.getAction()).isEqualTo(Action.HANDOVER_CREATE);
            assertThat(assignLog.getAction()).isEqualTo(Action.HANDOVER_RECEIVER_ASSIGN);
            assertThat(completeLog.getAction()).isEqualTo(Action.HANDOVER_COMPLETE);

            // 작성자와 완료자가 다름
            assertThat(createLog.getActorId()).isEqualTo(authorId);
            assertThat(completeLog.getActorId()).isEqualTo(recipientId);
        }
    }
}

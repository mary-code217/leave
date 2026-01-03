package com.hoho.leave.domain.notification.entity;

import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Notification 엔티티 테스트")
class NotificationTest {

    private Notification notification;
    private User mockRecipient;
    private NotificationType type;
    private String content;

    @BeforeEach
    void setUp() {
        mockRecipient = new User("test@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockRecipient, "id", 1L);
        ReflectionTestUtils.setField(mockRecipient, "username", "테스트유저");

        type = NotificationType.LEAVE_APPROVAL_REQUESTED;
        content = "휴가 승인 요청이 있습니다.";

        notification = Notification.create(mockRecipient, type, content);
    }

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {

        @Test
        @DisplayName("성공: create 정적 메서드로 알림을 생성한다")
        void create_Success() {
            // given
            User recipient = new User("user@example.com", UserRole.ROLE_USER);
            ReflectionTestUtils.setField(recipient, "id", 2L);
            NotificationType notificationType = NotificationType.LEAVE_STATUS_CHANGED;
            String notificationContent = "휴가 상태가 변경되었습니다.";

            // when
            Notification createdNotification = Notification.create(recipient, notificationType, notificationContent);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getRecipient()).isEqualTo(recipient);
            assertThat(createdNotification.getType()).isEqualTo(notificationType);
            assertThat(createdNotification.getContent()).isEqualTo(notificationContent);
            assertThat(createdNotification.getReadAt()).isNull();
        }

        @Test
        @DisplayName("성공: 휴가 승인 요청 알림을 생성한다")
        void create_LeaveApprovalRequested_Success() {
            // given
            NotificationType notificationType = NotificationType.LEAVE_APPROVAL_REQUESTED;
            String notificationContent = "홍길동님이 휴가 승인을 요청했습니다.";

            // when
            Notification createdNotification = Notification.create(mockRecipient, notificationType, notificationContent);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getType()).isEqualTo(NotificationType.LEAVE_APPROVAL_REQUESTED);
            assertThat(createdNotification.getContent()).contains("휴가 승인");
        }

        @Test
        @DisplayName("성공: 휴가 상태 변경 알림을 생성한다")
        void create_LeaveStatusChanged_Success() {
            // given
            NotificationType notificationType = NotificationType.LEAVE_STATUS_CHANGED;
            String notificationContent = "신청하신 휴가가 승인되었습니다.";

            // when
            Notification createdNotification = Notification.create(mockRecipient, notificationType, notificationContent);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getType()).isEqualTo(NotificationType.LEAVE_STATUS_CHANGED);
        }

        @Test
        @DisplayName("성공: 인수인계 배정 알림을 생성한다")
        void create_HandoverAssigned_Success() {
            // given
            NotificationType notificationType = NotificationType.HANDOVER_ASSIGNED;
            String notificationContent = "인수인계가 배정되었습니다.";

            // when
            Notification createdNotification = Notification.create(mockRecipient, notificationType, notificationContent);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getType()).isEqualTo(NotificationType.HANDOVER_ASSIGNED);
        }

        @Test
        @DisplayName("성공: 연차 잔여일수 조정 알림을 생성한다")
        void create_LeaveBalanceAdjusted_Success() {
            // given
            NotificationType notificationType = NotificationType.LEAVE_BALANCE_ADJUSTED;
            String notificationContent = "연차 잔여일수가 조정되었습니다. (15일 → 17일)";

            // when
            Notification createdNotification = Notification.create(mockRecipient, notificationType, notificationContent);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getType()).isEqualTo(NotificationType.LEAVE_BALANCE_ADJUSTED);
            assertThat(createdNotification.getContent()).contains("연차");
        }

        @Test
        @DisplayName("성공: null 값으로도 알림 생성이 가능하다")
        void create_WithNullValues_Success() {
            // given & when
            Notification createdNotification = Notification.create(null, null, null);

            // then
            assertThat(createdNotification).isNotNull();
            assertThat(createdNotification.getRecipient()).isNull();
            assertThat(createdNotification.getType()).isNull();
            assertThat(createdNotification.getContent()).isNull();
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: recipient 필드가 올바르게 저장된다")
        void recipient_StoredCorrectly() {
            // when
            User actualRecipient = notification.getRecipient();

            // then
            assertThat(actualRecipient).isEqualTo(mockRecipient);
            assertThat(actualRecipient.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("성공: type 필드가 올바르게 저장된다")
        void type_StoredCorrectly() {
            // given
            NotificationType expectedType = NotificationType.LEAVE_APPROVAL_REQUESTED;

            // when
            NotificationType actualType = notification.getType();

            // then
            assertThat(actualType).isEqualTo(expectedType);
        }

        @Test
        @DisplayName("성공: content 필드가 올바르게 저장된다")
        void content_StoredCorrectly() {
            // given
            String expectedContent = "휴가 승인 요청이 있습니다.";

            // when
            String actualContent = notification.getContent();

            // then
            assertThat(actualContent).isEqualTo(expectedContent);
        }

        @Test
        @DisplayName("성공: readAt 필드 초기값은 null이다")
        void readAt_InitiallyNull() {
            // when
            LocalDateTime readAt = notification.getReadAt();

            // then
            assertThat(readAt).isNull();
        }

        @Test
        @DisplayName("성공: id 필드는 초기값이 null이다")
        void id_InitiallyNull() {
            // when
            Long id = notification.getId();

            // then
            assertThat(id).isNull();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(notification, "id", expectedId);
            Long actualId = notification.getId();

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("알림 읽음 처리")
    class UpdateReadAt {

        @Test
        @DisplayName("성공: updateReadAt 호출 시 readAt이 현재 시간으로 설정된다")
        void updateReadAt_SetsCurrentTime() {
            // given
            assertThat(notification.getReadAt()).isNull();
            LocalDateTime beforeUpdate = LocalDateTime.now();

            // when
            notification.updateReadAt();

            // then
            assertThat(notification.getReadAt()).isNotNull();
            assertThat(notification.getReadAt()).isAfterOrEqualTo(beforeUpdate);
            assertThat(notification.getReadAt()).isBeforeOrEqualTo(LocalDateTime.now());
        }

        @Test
        @DisplayName("성공: 이미 읽은 알림도 다시 읽음 처리할 수 있다")
        void updateReadAt_AlreadyRead_UpdatesAgain() {
            // given
            notification.updateReadAt();
            LocalDateTime firstReadAt = notification.getReadAt();

            // when
            notification.updateReadAt();
            LocalDateTime secondReadAt = notification.getReadAt();

            // then
            assertThat(secondReadAt).isNotNull();
            assertThat(secondReadAt).isAfterOrEqualTo(firstReadAt);
        }

        @Test
        @DisplayName("성공: 읽음 처리 후 다른 필드는 변경되지 않는다")
        void updateReadAt_OtherFieldsUnchanged() {
            // given
            User originalRecipient = notification.getRecipient();
            NotificationType originalType = notification.getType();
            String originalContent = notification.getContent();

            // when
            notification.updateReadAt();

            // then
            assertThat(notification.getRecipient()).isEqualTo(originalRecipient);
            assertThat(notification.getType()).isEqualTo(originalType);
            assertThat(notification.getContent()).isEqualTo(originalContent);
        }
    }

    @Nested
    @DisplayName("다양한 NotificationType")
    class VariousNotificationTypes {

        @Test
        @DisplayName("성공: 모든 NotificationType으로 알림을 생성할 수 있다")
        void allNotificationTypes_Success() {
            // given
            NotificationType[] allTypes = NotificationType.values();

            // when & then
            for (NotificationType notificationType : allTypes) {
                Notification createdNotification = Notification.create(
                        mockRecipient,
                        notificationType,
                        "알림 내용: " + notificationType.name()
                );
                assertThat(createdNotification).isNotNull();
                assertThat(createdNotification.getType()).isEqualTo(notificationType);
            }
        }

        @Test
        @DisplayName("성공: NotificationType 개수가 4개이다")
        void notificationTypeCount_IsFour() {
            // when
            int count = NotificationType.values().length;

            // then
            assertThat(count).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("다양한 content 형식")
    class VariousContentFormats {

        @Test
        @DisplayName("성공: 한글 content를 저장한다")
        void koreanContent_Success() {
            // given
            String koreanContent = "홍길동님의 휴가 신청이 승인되었습니다.";

            // when
            Notification createdNotification = Notification.create(mockRecipient, type, koreanContent);

            // then
            assertThat(createdNotification.getContent()).isEqualTo(koreanContent);
            assertThat(createdNotification.getContent()).contains("휴가");
        }

        @Test
        @DisplayName("성공: 영문 content를 저장한다")
        void englishContent_Success() {
            // given
            String englishContent = "Your leave request has been approved.";

            // when
            Notification createdNotification = Notification.create(mockRecipient, type, englishContent);

            // then
            assertThat(createdNotification.getContent()).isEqualTo(englishContent);
        }

        @Test
        @DisplayName("성공: 긴 content 문자열도 저장 가능하다")
        void longContent_Success() {
            // given
            String longContent = "이것은 매우 긴 알림 내용입니다. ".repeat(10);

            // when
            Notification createdNotification = Notification.create(mockRecipient, type, longContent);

            // then
            assertThat(createdNotification.getContent()).isEqualTo(longContent);
            assertThat(createdNotification.getContent().length()).isGreaterThan(100);
        }

        @Test
        @DisplayName("성공: 특수문자가 포함된 content를 저장한다")
        void specialCharactersContent_Success() {
            // given
            String specialContent = "휴가 기간: 2025-01-01 ~ 2025-01-03 (3일) [연차]";

            // when
            Notification createdNotification = Notification.create(mockRecipient, type, specialContent);

            // then
            assertThat(createdNotification.getContent()).isEqualTo(specialContent);
            assertThat(createdNotification.getContent()).contains("~");
            assertThat(createdNotification.getContent()).contains("[연차]");
        }

        @Test
        @DisplayName("성공: 빈 문자열 content도 저장 가능하다")
        void emptyContent_Success() {
            // given
            String emptyContent = "";

            // when
            Notification createdNotification = Notification.create(mockRecipient, type, emptyContent);

            // then
            assertThat(createdNotification.getContent()).isEmpty();
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
            ReflectionTestUtils.setField(notification, "createdAt", expectedCreatedAt);
            LocalDateTime actualCreatedAt = notification.getCreatedAt();

            // then
            assertThat(actualCreatedAt).isEqualTo(expectedCreatedAt);
        }

        @Test
        @DisplayName("성공: updatedAt 필드를 ReflectionTestUtils로 설정할 수 있다")
        void updatedAt_CanBeSetWithReflection() {
            // given
            LocalDateTime expectedUpdatedAt = LocalDateTime.of(2025, 1, 2, 15, 30, 0);

            // when
            ReflectionTestUtils.setField(notification, "updatedAt", expectedUpdatedAt);
            LocalDateTime actualUpdatedAt = notification.getUpdatedAt();

            // then
            assertThat(actualUpdatedAt).isEqualTo(expectedUpdatedAt);
        }

        @Test
        @DisplayName("성공: createdAt과 updatedAt 초기값은 null이다")
        void timestamps_InitiallyNull() {
            // when
            LocalDateTime createdAt = notification.getCreatedAt();
            LocalDateTime updatedAt = notification.getUpdatedAt();

            // then
            assertThat(createdAt).isNull();
            assertThat(updatedAt).isNull();
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 휴가 신청 전체 워크플로우 알림 생성")
        void leaveRequestWorkflow_Success() {
            // given
            User approver = new User("approver@example.com", UserRole.ROLE_ADMIN);
            ReflectionTestUtils.setField(approver, "id", 2L);
            User applicant = mockRecipient;

            // when - 신청자 → 승인자에게 승인 요청 알림
            Notification requestNotification = Notification.create(
                    approver,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "테스트유저님이 휴가 승인을 요청했습니다."
            );

            // when - 승인자 → 신청자에게 상태 변경 알림
            Notification statusNotification = Notification.create(
                    applicant,
                    NotificationType.LEAVE_STATUS_CHANGED,
                    "신청하신 휴가가 승인되었습니다."
            );

            // then
            assertThat(requestNotification.getRecipient()).isEqualTo(approver);
            assertThat(requestNotification.getType()).isEqualTo(NotificationType.LEAVE_APPROVAL_REQUESTED);

            assertThat(statusNotification.getRecipient()).isEqualTo(applicant);
            assertThat(statusNotification.getType()).isEqualTo(NotificationType.LEAVE_STATUS_CHANGED);
        }

        @Test
        @DisplayName("성공: 인수인계 배정 알림 시나리오")
        void handoverAssignment_Success() {
            // given
            User recipient1 = new User("recipient1@example.com", UserRole.ROLE_USER);
            User recipient2 = new User("recipient2@example.com", UserRole.ROLE_USER);
            ReflectionTestUtils.setField(recipient1, "id", 10L);
            ReflectionTestUtils.setField(recipient2, "id", 11L);

            // when
            Notification notification1 = Notification.create(
                    recipient1,
                    NotificationType.HANDOVER_ASSIGNED,
                    "테스트유저님의 인수인계가 배정되었습니다."
            );
            Notification notification2 = Notification.create(
                    recipient2,
                    NotificationType.HANDOVER_ASSIGNED,
                    "테스트유저님의 인수인계가 배정되었습니다."
            );

            // then
            assertThat(notification1.getRecipient()).isNotEqualTo(notification2.getRecipient());
            assertThat(notification1.getType()).isEqualTo(notification2.getType());
            assertThat(notification1.getContent()).isEqualTo(notification2.getContent());
        }

        @Test
        @DisplayName("성공: 알림 생성 후 읽음 처리 시나리오")
        void createAndRead_Success() {
            // given
            Notification newNotification = Notification.create(
                    mockRecipient,
                    NotificationType.LEAVE_BALANCE_ADJUSTED,
                    "연차 잔여일수가 조정되었습니다."
            );
            ReflectionTestUtils.setField(newNotification, "id", 1L);
            ReflectionTestUtils.setField(newNotification, "createdAt", LocalDateTime.now().minusMinutes(5));

            assertThat(newNotification.getReadAt()).isNull();

            // when
            newNotification.updateReadAt();

            // then
            assertThat(newNotification.getId()).isEqualTo(1L);
            assertThat(newNotification.getReadAt()).isNotNull();
            assertThat(newNotification.getReadAt()).isAfter(newNotification.getCreatedAt());
        }

        @Test
        @DisplayName("성공: 동일 수신자에게 여러 알림 생성")
        void multipleNotificationsToSameRecipient_Success() {
            // given & when
            Notification notification1 = Notification.create(
                    mockRecipient,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "첫 번째 휴가 승인 요청"
            );
            Notification notification2 = Notification.create(
                    mockRecipient,
                    NotificationType.HANDOVER_ASSIGNED,
                    "인수인계 배정"
            );
            Notification notification3 = Notification.create(
                    mockRecipient,
                    NotificationType.LEAVE_BALANCE_ADJUSTED,
                    "연차 잔여일수 조정"
            );

            // then
            assertThat(notification1.getRecipient()).isEqualTo(notification2.getRecipient());
            assertThat(notification2.getRecipient()).isEqualTo(notification3.getRecipient());

            assertThat(notification1.getType()).isNotEqualTo(notification2.getType());
            assertThat(notification2.getType()).isNotEqualTo(notification3.getType());
        }
    }
}

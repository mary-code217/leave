package com.hoho.leave.domain.notification.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.notification.dto.response.NotificationDetailResponse;
import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.entity.Notification;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.notification.repository.NotificationRepository;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.entity.UserRole;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService 테스트")
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    private User mockUser;
    private User mockUser2;
    private Notification mockNotification;

    @BeforeEach
    void setUp() {
        // Mock User 생성
        mockUser = new User("test@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        ReflectionTestUtils.setField(mockUser, "username", "테스트유저");

        mockUser2 = new User("test2@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockUser2, "id", 2L);
        ReflectionTestUtils.setField(mockUser2, "username", "테스트유저2");

        // Mock Notification 생성
        mockNotification = Notification.create(
                mockUser,
                NotificationType.LEAVE_APPROVAL_REQUESTED,
                "휴가 승인 요청이 도착했습니다."
        );
        ReflectionTestUtils.setField(mockNotification, "id", 1L);
        ReflectionTestUtils.setField(mockNotification, "createdAt", LocalDateTime.now());
    }

    @Nested
    @DisplayName("알림 생성")
    class CreateNotification {

        @Test
        @DisplayName("성공: 단일 사용자에게 알림을 생성한다")
        void createNotification_Success() {
            // given
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createNotification(
                    mockUser,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "휴가 승인 요청이 도착했습니다."
            );

            // then
            verify(notificationRepository, times(1)).save(any(Notification.class));
        }

        @Test
        @DisplayName("성공: 여러 사용자에게 동일한 알림을 생성한다")
        void createManyNotification_Success() {
            // given
            List<User> recipients = List.of(mockUser, mockUser2);
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createManyNotification(
                    recipients,
                    NotificationType.LEAVE_STATUS_CHANGED,
                    "휴가 상태가 변경되었습니다."
            );

            // then
            verify(notificationRepository, times(2)).save(any(Notification.class));
        }

        @Test
        @DisplayName("성공: 빈 수신자 목록으로 알림 생성 시 저장되지 않는다")
        void createManyNotification_EmptyRecipients() {
            // given
            List<User> emptyRecipients = List.of();

            // when
            notificationService.createManyNotification(
                    emptyRecipients,
                    NotificationType.HANDOVER_ASSIGNED,
                    "인수인계가 배정되었습니다."
            );

            // then
            verify(notificationRepository, never()).save(any(Notification.class));
        }
    }

    @Nested
    @DisplayName("알림 읽음 처리")
    class UpdateReadAt {

        @Test
        @DisplayName("성공: 알림을 읽음 처리한다")
        void updateReadAt_Success() {
            // given
            Long notificationId = 1L;
            given(notificationRepository.findById(notificationId)).willReturn(Optional.of(mockNotification));

            // when
            notificationService.updateReadAt(notificationId);

            // then
            verify(notificationRepository).findById(notificationId);
            assertThat(mockNotification.getReadAt()).isNotNull();
        }

        @Test
        @DisplayName("실패: 존재하지 않는 알림 읽음 처리 시 예외 발생")
        void updateReadAt_NotFound_ThrowsException() {
            // given
            Long notificationId = 999L;
            given(notificationRepository.findById(notificationId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> notificationService.updateReadAt(notificationId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Notification");

            verify(notificationRepository).findById(notificationId);
        }
    }

    @Nested
    @DisplayName("사용자 알림 목록 조회")
    class GetAllUserNotifications {

        @Test
        @DisplayName("성공: 사용자의 알림 목록을 페이지네이션하여 조회한다")
        void getAllUserNotifications_Success() {
            // given
            Long userId = 1L;
            Integer page = 1;
            Integer size = 10;

            Notification notification2 = Notification.create(
                    mockUser,
                    NotificationType.LEAVE_STATUS_CHANGED,
                    "휴가가 승인되었습니다."
            );
            ReflectionTestUtils.setField(notification2, "id", 2L);
            ReflectionTestUtils.setField(notification2, "createdAt", LocalDateTime.now().minusHours(1));

            List<Notification> notifications = List.of(mockNotification, notification2);
            Page<Notification> notificationPage = new PageImpl<>(notifications);

            given(notificationRepository.findByRecipientId(eq(userId), any(Pageable.class)))
                    .willReturn(notificationPage);

            // when
            NotificationListResponse response = notificationService.getAllUserNotifications(userId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getNotifications()).hasSize(2);
            assertThat(response.getPage()).isEqualTo(1);
            assertThat(response.getSize()).isEqualTo(2);
            verify(notificationRepository).findByRecipientId(eq(userId), any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 알림이 없는 경우 빈 목록을 반환한다")
        void getAllUserNotifications_EmptyList() {
            // given
            Long userId = 1L;
            Integer page = 1;
            Integer size = 10;

            Page<Notification> emptyPage = new PageImpl<>(List.of());
            given(notificationRepository.findByRecipientId(eq(userId), any(Pageable.class)))
                    .willReturn(emptyPage);

            // when
            NotificationListResponse response = notificationService.getAllUserNotifications(userId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getNotifications()).isEmpty();
            assertThat(response.getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지 정보가 올바르게 반환된다")
        void getAllUserNotifications_PaginationInfo() {
            // given
            Long userId = 1L;
            Integer page = 2;
            Integer size = 5;

            List<Notification> notifications = List.of(mockNotification);
            Page<Notification> notificationPage = new PageImpl<>(notifications);

            given(notificationRepository.findByRecipientId(eq(userId), any(Pageable.class)))
                    .willReturn(notificationPage);

            // when
            NotificationListResponse response = notificationService.getAllUserNotifications(userId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getNotifications()).hasSize(1);
            verify(notificationRepository).findByRecipientId(eq(userId), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("알림 유형별 생성")
    class CreateNotificationByType {

        @Test
        @DisplayName("성공: 휴가 승인 요청 알림을 생성한다")
        void createNotification_LeaveApprovalRequested() {
            // given
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createNotification(
                    mockUser,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "홍길동님이 휴가 승인을 요청했습니다."
            );

            // then
            verify(notificationRepository).save(any(Notification.class));
        }

        @Test
        @DisplayName("성공: 휴가 상태 변경 알림을 생성한다")
        void createNotification_LeaveStatusChanged() {
            // given
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createNotification(
                    mockUser,
                    NotificationType.LEAVE_STATUS_CHANGED,
                    "휴가 신청이 승인되었습니다."
            );

            // then
            verify(notificationRepository).save(any(Notification.class));
        }

        @Test
        @DisplayName("성공: 인수인계 배정 알림을 생성한다")
        void createNotification_HandoverAssigned() {
            // given
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createNotification(
                    mockUser,
                    NotificationType.HANDOVER_ASSIGNED,
                    "홍길동님의 업무를 인수인계 받았습니다."
            );

            // then
            verify(notificationRepository).save(any(Notification.class));
        }

        @Test
        @DisplayName("성공: 연차 잔여일수 조정 알림을 생성한다")
        void createNotification_LeaveBalanceAdjusted() {
            // given
            given(notificationRepository.save(any(Notification.class))).willReturn(mockNotification);

            // when
            notificationService.createNotification(
                    mockUser,
                    NotificationType.LEAVE_BALANCE_ADJUSTED,
                    "연차 잔여일수가 15일로 조정되었습니다."
            );

            // then
            verify(notificationRepository).save(any(Notification.class));
        }
    }
}

package com.hoho.leave.domain.notification.repository;

import com.hoho.leave.domain.notification.entity.Notification;
import com.hoho.leave.domain.notification.entity.NotificationType;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.entity.UserRole;
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
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationRepository 테스트")
class NotificationRepositoryTest {

    @Mock
    private NotificationRepository notificationRepository;

    private User mockUser;
    private User mockUser2;
    private Notification mockNotification;
    private Notification mockNotification2;

    @BeforeEach
    void setUp() {
        // Mock User 생성
        mockUser = new User("test@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockUser, "id", 1L);
        ReflectionTestUtils.setField(mockUser, "username", "테스트유저1");

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

        mockNotification2 = Notification.create(
                mockUser,
                NotificationType.LEAVE_STATUS_CHANGED,
                "휴가가 승인되었습니다."
        );
        ReflectionTestUtils.setField(mockNotification2, "id", 2L);
        ReflectionTestUtils.setField(mockNotification2, "createdAt", LocalDateTime.now().minusHours(1));
    }

    @Nested
    @DisplayName("수신자 ID로 알림 조회")
    class FindByRecipientId {

        @Test
        @DisplayName("성공: 특정 사용자의 알림 목록을 페이지네이션하여 조회한다")
        void findByRecipientId_Success() {
            // given
            Long userId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
            List<Notification> notifications = List.of(mockNotification, mockNotification2);
            Page<Notification> notificationPage = new PageImpl<>(notifications, pageRequest, 2);

            given(notificationRepository.findByRecipientId(eq(userId), any(PageRequest.class)))
                    .willReturn(notificationPage);

            // when
            Page<Notification> result = notificationRepository.findByRecipientId(userId, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            verify(notificationRepository).findByRecipientId(userId, pageRequest);
        }

        @Test
        @DisplayName("성공: 알림이 없는 사용자 조회 시 빈 목록 반환")
        void findByRecipientId_EmptyResult() {
            // given
            Long userId = 1L;
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
            Page<Notification> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(notificationRepository.findByRecipientId(eq(userId), any(PageRequest.class)))
                    .willReturn(emptyPage);

            // when
            Page<Notification> result = notificationRepository.findByRecipientId(userId, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 다른 사용자의 알림은 조회되지 않는다")
        void findByRecipientId_OnlyOwnNotifications() {
            // given
            Long userId1 = 1L;
            Long userId2 = 2L;
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));

            Notification user2Notification = Notification.create(
                    mockUser2,
                    NotificationType.LEAVE_STATUS_CHANGED,
                    "사용자2의 알림"
            );
            ReflectionTestUtils.setField(user2Notification, "id", 3L);

            // 사용자1의 알림만 반환
            Page<Notification> user1Page = new PageImpl<>(List.of(mockNotification), pageRequest, 1);
            given(notificationRepository.findByRecipientId(eq(userId1), any(PageRequest.class)))
                    .willReturn(user1Page);

            // 사용자2의 알림만 반환
            Page<Notification> user2Page = new PageImpl<>(List.of(user2Notification), pageRequest, 1);
            given(notificationRepository.findByRecipientId(eq(userId2), any(PageRequest.class)))
                    .willReturn(user2Page);

            // when
            Page<Notification> result1 = notificationRepository.findByRecipientId(userId1, pageRequest);
            Page<Notification> result2 = notificationRepository.findByRecipientId(userId2, pageRequest);

            // then
            assertThat(result1.getContent()).hasSize(1);
            assertThat(result1.getContent().get(0).getRecipient().getId()).isEqualTo(userId1);

            assertThat(result2.getContent()).hasSize(1);
            assertThat(result2.getContent().get(0).getRecipient().getId()).isEqualTo(userId2);
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findByRecipientId_Pagination() {
            // given
            Long userId = 1L;

            // 15개의 알림 생성
            List<Notification> allNotifications = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                Notification notification = Notification.create(
                        mockUser,
                        NotificationType.LEAVE_APPROVAL_REQUESTED,
                        "알림 " + i
                );
                ReflectionTestUtils.setField(notification, "id", (long) (i + 1));
                ReflectionTestUtils.setField(notification, "createdAt", LocalDateTime.now().minusMinutes(i));
                allNotifications.add(notification);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("createdAt")));
            PageRequest pageRequest2 = PageRequest.of(1, 5, Sort.by(Sort.Order.desc("createdAt")));
            PageRequest pageRequest3 = PageRequest.of(2, 5, Sort.by(Sort.Order.desc("createdAt")));

            Page<Notification> page1 = new PageImpl<>(allNotifications.subList(0, 5), pageRequest1, 15);
            Page<Notification> page2 = new PageImpl<>(allNotifications.subList(5, 10), pageRequest2, 15);
            Page<Notification> page3 = new PageImpl<>(allNotifications.subList(10, 15), pageRequest3, 15);

            given(notificationRepository.findByRecipientId(eq(userId), eq(pageRequest1))).willReturn(page1);
            given(notificationRepository.findByRecipientId(eq(userId), eq(pageRequest2))).willReturn(page2);
            given(notificationRepository.findByRecipientId(eq(userId), eq(pageRequest3))).willReturn(page3);

            // when
            Page<Notification> result1 = notificationRepository.findByRecipientId(userId, pageRequest1);
            Page<Notification> result2 = notificationRepository.findByRecipientId(userId, pageRequest2);
            Page<Notification> result3 = notificationRepository.findByRecipientId(userId, pageRequest3);

            // then
            assertThat(result1.getContent()).hasSize(5);
            assertThat(result2.getContent()).hasSize(5);
            assertThat(result3.getContent()).hasSize(5);
            assertThat(result1.getTotalElements()).isEqualTo(15);
            assertThat(result1.getTotalPages()).isEqualTo(3);
            assertThat(result1.isFirst()).isTrue();
            assertThat(result3.isLast()).isTrue();
        }

        @Test
        @DisplayName("성공: 존재하지 않는 사용자 ID로 조회 시 빈 목록 반환")
        void findByRecipientId_NonExistentUser() {
            // given
            Long nonExistentUserId = 999L;
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("createdAt")));
            Page<Notification> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(notificationRepository.findByRecipientId(eq(nonExistentUserId), any(PageRequest.class)))
                    .willReturn(emptyPage);

            // when
            Page<Notification> result = notificationRepository.findByRecipientId(nonExistentUserId, pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
        }
    }

    @Nested
    @DisplayName("알림 저장")
    class SaveNotification {

        @Test
        @DisplayName("성공: 알림을 저장한다")
        void save_Success() {
            // given
            Notification newNotification = Notification.create(
                    mockUser,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "새로운 휴가 승인 요청이 도착했습니다."
            );

            Notification savedNotification = Notification.create(
                    mockUser,
                    NotificationType.LEAVE_APPROVAL_REQUESTED,
                    "새로운 휴가 승인 요청이 도착했습니다."
            );
            ReflectionTestUtils.setField(savedNotification, "id", 100L);
            ReflectionTestUtils.setField(savedNotification, "createdAt", LocalDateTime.now());

            given(notificationRepository.save(any(Notification.class))).willReturn(savedNotification);

            // when
            Notification result = notificationRepository.save(newNotification);

            // then
            assertThat(result.getId()).isEqualTo(100L);
            assertThat(result.getRecipient().getId()).isEqualTo(mockUser.getId());
            assertThat(result.getType()).isEqualTo(NotificationType.LEAVE_APPROVAL_REQUESTED);
            assertThat(result.getContent()).isEqualTo("새로운 휴가 승인 요청이 도착했습니다.");
            assertThat(result.getReadAt()).isNull();
            verify(notificationRepository).save(newNotification);
        }

        @Test
        @DisplayName("성공: 다양한 알림 유형을 저장한다")
        void save_DifferentTypes() {
            // given
            Notification notification1 = Notification.create(mockUser, NotificationType.LEAVE_APPROVAL_REQUESTED, "알림1");
            Notification notification2 = Notification.create(mockUser, NotificationType.LEAVE_STATUS_CHANGED, "알림2");
            Notification notification3 = Notification.create(mockUser, NotificationType.HANDOVER_ASSIGNED, "알림3");
            Notification notification4 = Notification.create(mockUser, NotificationType.LEAVE_BALANCE_ADJUSTED, "알림4");

            given(notificationRepository.save(any(Notification.class))).willAnswer(invocation -> {
                Notification n = invocation.getArgument(0);
                ReflectionTestUtils.setField(n, "id", (long) (Math.random() * 1000));
                return n;
            });

            // when
            Notification saved1 = notificationRepository.save(notification1);
            Notification saved2 = notificationRepository.save(notification2);
            Notification saved3 = notificationRepository.save(notification3);
            Notification saved4 = notificationRepository.save(notification4);

            // then
            assertThat(saved1.getType()).isEqualTo(NotificationType.LEAVE_APPROVAL_REQUESTED);
            assertThat(saved2.getType()).isEqualTo(NotificationType.LEAVE_STATUS_CHANGED);
            assertThat(saved3.getType()).isEqualTo(NotificationType.HANDOVER_ASSIGNED);
            assertThat(saved4.getType()).isEqualTo(NotificationType.LEAVE_BALANCE_ADJUSTED);
        }
    }

    @Nested
    @DisplayName("알림 조회 (findById)")
    class FindById {

        @Test
        @DisplayName("성공: ID로 알림을 조회한다")
        void findById_Success() {
            // given
            Long notificationId = 1L;
            given(notificationRepository.findById(notificationId)).willReturn(Optional.of(mockNotification));

            // when
            Optional<Notification> result = notificationRepository.findById(notificationId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getContent()).isEqualTo("휴가 승인 요청이 도착했습니다.");
            verify(notificationRepository).findById(notificationId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 조회 시 빈 Optional 반환")
        void findById_NotFound() {
            // given
            Long nonExistentId = 999L;
            given(notificationRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<Notification> result = notificationRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(notificationRepository).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("알림 읽음 처리")
    class UpdateReadAt {

        @Test
        @DisplayName("성공: 알림을 읽음 처리한다")
        void updateReadAt_Success() {
            // given
            assertThat(mockNotification.getReadAt()).isNull();

            // when
            mockNotification.updateReadAt();

            // then
            assertThat(mockNotification.getReadAt()).isNotNull();
        }

        @Test
        @DisplayName("성공: 읽음 처리된 알림의 readAt이 현재 시간에 가깝다")
        void updateReadAt_TimeIsRecent() {
            // given
            LocalDateTime beforeUpdate = LocalDateTime.now();

            // when
            mockNotification.updateReadAt();

            // then
            LocalDateTime afterUpdate = LocalDateTime.now();
            assertThat(mockNotification.getReadAt()).isAfterOrEqualTo(beforeUpdate);
            assertThat(mockNotification.getReadAt()).isBeforeOrEqualTo(afterUpdate);
        }
    }
}

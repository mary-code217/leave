package com.hoho.leave.domain.notification.controller;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.notification.dto.response.NotificationDetailResponse;
import com.hoho.leave.domain.notification.dto.response.NotificationListResponse;
import com.hoho.leave.domain.notification.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationController 테스트")
class NotificationControllerTest {

    @InjectMocks
    private NotificationController notificationController;

    @Mock
    private NotificationService notificationService;

    private NotificationListResponse mockListResponse;
    private NotificationDetailResponse mockDetailResponse;

    @BeforeEach
    void setUp() {
        // Mock NotificationDetailResponse 생성
        mockDetailResponse = new NotificationDetailResponse();
        mockDetailResponse.setNotificationId(1L);
        mockDetailResponse.setContent("휴가 승인 요청이 도착했습니다.");
        mockDetailResponse.setCreateAt(LocalDateTime.now());
        mockDetailResponse.setReadAt(null);

        // Mock NotificationListResponse 생성
        mockListResponse = new NotificationListResponse();
        mockListResponse.setPage(1);
        mockListResponse.setSize(10);
        mockListResponse.setNotifications(List.of(mockDetailResponse));
        mockListResponse.setTotalPage(1);
        mockListResponse.setTotalElement(1L);
        mockListResponse.setFirstPage(true);
        mockListResponse.setLastPage(true);
    }

    @Nested
    @DisplayName("알림 목록 조회")
    class GetAllNotifications {

        @Test
        @DisplayName("성공: 사용자의 알림 목록을 조회한다")
        void getAllNotifications_Success() {
            // given
            Long userId = 1L;
            Integer page = 1;
            Integer size = 10;

            given(notificationService.getAllUserNotifications(userId, page, size))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<NotificationListResponse> response =
                    notificationController.getAllNotifications(userId, page, size);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getNotifications()).hasSize(1);
            assertThat(response.getBody().getPage()).isEqualTo(1);
            verify(notificationService).getAllUserNotifications(userId, page, size);
        }

        @Test
        @DisplayName("성공: 기본 페이지네이션 값으로 조회한다")
        void getAllNotifications_DefaultPagination() {
            // given
            Long userId = 1L;
            Integer defaultPage = 1;
            Integer defaultSize = 10;

            given(notificationService.getAllUserNotifications(userId, defaultPage, defaultSize))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<NotificationListResponse> response =
                    notificationController.getAllNotifications(userId, defaultPage, defaultSize);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(notificationService).getAllUserNotifications(userId, defaultPage, defaultSize);
        }

        @Test
        @DisplayName("성공: 빈 알림 목록을 조회한다")
        void getAllNotifications_EmptyList() {
            // given
            Long userId = 1L;
            Integer page = 1;
            Integer size = 10;

            NotificationListResponse emptyResponse = new NotificationListResponse();
            emptyResponse.setPage(1);
            emptyResponse.setSize(10);
            emptyResponse.setNotifications(List.of());
            emptyResponse.setTotalPage(0);
            emptyResponse.setTotalElement(0L);
            emptyResponse.setFirstPage(true);
            emptyResponse.setLastPage(true);

            given(notificationService.getAllUserNotifications(userId, page, size))
                    .willReturn(emptyResponse);

            // when
            ResponseEntity<NotificationListResponse> response =
                    notificationController.getAllNotifications(userId, page, size);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getNotifications()).isEmpty();
            assertThat(response.getBody().getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 여러 알림이 있는 목록을 조회한다")
        void getAllNotifications_MultipleNotifications() {
            // given
            Long userId = 1L;
            Integer page = 1;
            Integer size = 10;

            NotificationDetailResponse detail2 = new NotificationDetailResponse();
            detail2.setNotificationId(2L);
            detail2.setContent("휴가가 승인되었습니다.");
            detail2.setCreateAt(LocalDateTime.now().minusHours(1));
            detail2.setReadAt(LocalDateTime.now());

            NotificationDetailResponse detail3 = new NotificationDetailResponse();
            detail3.setNotificationId(3L);
            detail3.setContent("인수인계가 배정되었습니다.");
            detail3.setCreateAt(LocalDateTime.now().minusHours(2));
            detail3.setReadAt(null);

            NotificationListResponse multiResponse = new NotificationListResponse();
            multiResponse.setPage(1);
            multiResponse.setSize(10);
            multiResponse.setNotifications(List.of(mockDetailResponse, detail2, detail3));
            multiResponse.setTotalPage(1);
            multiResponse.setTotalElement(3L);
            multiResponse.setFirstPage(true);
            multiResponse.setLastPage(true);

            given(notificationService.getAllUserNotifications(userId, page, size))
                    .willReturn(multiResponse);

            // when
            ResponseEntity<NotificationListResponse> response =
                    notificationController.getAllNotifications(userId, page, size);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getNotifications()).hasSize(3);
            assertThat(response.getBody().getTotalElement()).isEqualTo(3L);
        }

        @Test
        @DisplayName("성공: 페이지네이션 정보가 올바르게 반환된다")
        void getAllNotifications_PaginationInfo() {
            // given
            Long userId = 1L;
            Integer page = 2;
            Integer size = 5;

            NotificationListResponse paginatedResponse = new NotificationListResponse();
            paginatedResponse.setPage(2);
            paginatedResponse.setSize(5);
            paginatedResponse.setNotifications(List.of(mockDetailResponse));
            paginatedResponse.setTotalPage(3);
            paginatedResponse.setTotalElement(15L);
            paginatedResponse.setFirstPage(false);
            paginatedResponse.setLastPage(false);

            given(notificationService.getAllUserNotifications(userId, page, size))
                    .willReturn(paginatedResponse);

            // when
            ResponseEntity<NotificationListResponse> response =
                    notificationController.getAllNotifications(userId, page, size);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getPage()).isEqualTo(2);
            assertThat(response.getBody().getSize()).isEqualTo(5);
            assertThat(response.getBody().getTotalPage()).isEqualTo(3);
            assertThat(response.getBody().getFirstPage()).isFalse();
            assertThat(response.getBody().getLastPage()).isFalse();
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
            doNothing().when(notificationService).updateReadAt(notificationId);

            // when
            ResponseEntity<?> response = notificationController.updateReadAt(notificationId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("읽음 처리 완료");
            verify(notificationService).updateReadAt(notificationId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 알림 읽음 처리 시 예외 발생")
        void updateReadAt_NotFound_ThrowsException() {
            // given
            Long notificationId = 999L;
            doThrow(new NotFoundException("Not Found Notification : " + notificationId))
                    .when(notificationService).updateReadAt(notificationId);

            // when & then
            assertThatThrownBy(() -> notificationController.updateReadAt(notificationId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Notification");

            verify(notificationService).updateReadAt(notificationId);
        }

        @Test
        @DisplayName("성공: 여러 알림을 순차적으로 읽음 처리한다")
        void updateReadAt_MultipleNotifications() {
            // given
            Long notificationId1 = 1L;
            Long notificationId2 = 2L;
            doNothing().when(notificationService).updateReadAt(any(Long.class));

            // when
            ResponseEntity<?> response1 = notificationController.updateReadAt(notificationId1);
            ResponseEntity<?> response2 = notificationController.updateReadAt(notificationId2);

            // then
            assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response2.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(notificationService).updateReadAt(notificationId1);
            verify(notificationService).updateReadAt(notificationId2);
        }
    }
}

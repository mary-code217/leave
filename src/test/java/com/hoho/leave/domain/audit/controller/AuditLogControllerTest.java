package com.hoho.leave.domain.audit.controller;

import com.hoho.leave.domain.audit.dto.response.AuditLogDetailResponse;
import com.hoho.leave.domain.audit.dto.response.AuditLogListResponse;
import com.hoho.leave.domain.audit.entity.Action;
import com.hoho.leave.domain.audit.service.AuditLogService;
import com.hoho.leave.domain.audit.service.AuditObjectType;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditLogController 테스트")
class AuditLogControllerTest {

    @InjectMocks
    private AuditLogController auditLogController;

    @Mock
    private AuditLogService auditLogService;

    private AuditLogListResponse mockListResponse;
    private AuditLogDetailResponse mockDetailResponse;
    private AuditLogDetailResponse mockDetailResponse2;

    @BeforeEach
    void setUp() {
        // Mock AuditLogDetailResponse 생성
        mockDetailResponse = new AuditLogDetailResponse();
        mockDetailResponse.setId(1L);
        mockDetailResponse.setAction(Action.USER_JOIN);
        mockDetailResponse.setUserId(1L);
        mockDetailResponse.setUsername("테스트유저");
        mockDetailResponse.setEmployeeNo("EMP001");
        mockDetailResponse.setObjectId(1L);
        mockDetailResponse.setObjectType(AuditObjectType.USER);
        mockDetailResponse.setSummary("테스트유저가 회원가입을 완료했습니다.");
        mockDetailResponse.setOccurredAt(LocalDateTime.now());

        mockDetailResponse2 = new AuditLogDetailResponse();
        mockDetailResponse2.setId(2L);
        mockDetailResponse2.setAction(Action.LEAVE_APPLY);
        mockDetailResponse2.setUserId(2L);
        mockDetailResponse2.setUsername("관리자유저");
        mockDetailResponse2.setEmployeeNo("EMP002");
        mockDetailResponse2.setObjectId(10L);
        mockDetailResponse2.setObjectType(AuditObjectType.LEAVE_REQUEST);
        mockDetailResponse2.setSummary("관리자유저가 휴가를 신청했습니다.");
        mockDetailResponse2.setOccurredAt(LocalDateTime.now().minusHours(1));

        // Mock AuditLogListResponse 생성
        mockListResponse = new AuditLogListResponse();
        mockListResponse.setPage(1);
        mockListResponse.setSize(10);
        mockListResponse.setAuditLogs(List.of(mockDetailResponse, mockDetailResponse2));
        mockListResponse.setTotalPage(1);
        mockListResponse.setTotalElement(2L);
        mockListResponse.setFirstPage(true);
        mockListResponse.setLastPage(true);
    }

    @Nested
    @DisplayName("감사 로그 목록 조회")
    class GetAllLogs {

        @Test
        @DisplayName("성공: 전체 감사 로그 목록을 조회한다")
        void getAllLogs_Success() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAuditLogs()).hasSize(2);
            assertThat(response.getBody().getPage()).isEqualTo(1);
            verify(auditLogService).getAllLogs(page, size, objectType);
        }

        @Test
        @DisplayName("성공: 기본 페이지네이션 값으로 조회한다")
        void getAllLogs_DefaultPagination() {
            // given
            Integer defaultPage = 1;
            Integer defaultSize = 10;
            String defaultObjectType = "all";

            given(auditLogService.getAllLogs(defaultPage, defaultSize, defaultObjectType))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(defaultPage, defaultSize, defaultObjectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(auditLogService).getAllLogs(defaultPage, defaultSize, defaultObjectType);
        }

        @Test
        @DisplayName("성공: 빈 감사 로그 목록을 조회한다")
        void getAllLogs_EmptyList() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            AuditLogListResponse emptyResponse = new AuditLogListResponse();
            emptyResponse.setPage(1);
            emptyResponse.setSize(10);
            emptyResponse.setAuditLogs(List.of());
            emptyResponse.setTotalPage(0);
            emptyResponse.setTotalElement(0L);
            emptyResponse.setFirstPage(true);
            emptyResponse.setLastPage(true);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(emptyResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAuditLogs()).isEmpty();
            assertThat(response.getBody().getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 특정 객체 유형별로 감사 로그를 조회한다")
        void getAllLogs_ByObjectType() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = AuditObjectType.USER;

            AuditLogListResponse userLogsResponse = new AuditLogListResponse();
            userLogsResponse.setPage(1);
            userLogsResponse.setSize(10);
            userLogsResponse.setAuditLogs(List.of(mockDetailResponse));
            userLogsResponse.setTotalPage(1);
            userLogsResponse.setTotalElement(1L);
            userLogsResponse.setFirstPage(true);
            userLogsResponse.setLastPage(true);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(userLogsResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAuditLogs()).hasSize(1);
            assertThat(response.getBody().getAuditLogs().get(0).getObjectType())
                    .isEqualTo(AuditObjectType.USER);
            verify(auditLogService).getAllLogs(page, size, objectType);
        }

        @Test
        @DisplayName("성공: 휴가 신청 객체 유형으로 감사 로그를 조회한다")
        void getAllLogs_ByLeaveRequestObjectType() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = AuditObjectType.LEAVE_REQUEST;

            AuditLogListResponse leaveLogsResponse = new AuditLogListResponse();
            leaveLogsResponse.setPage(1);
            leaveLogsResponse.setSize(10);
            leaveLogsResponse.setAuditLogs(List.of(mockDetailResponse2));
            leaveLogsResponse.setTotalPage(1);
            leaveLogsResponse.setTotalElement(1L);
            leaveLogsResponse.setFirstPage(true);
            leaveLogsResponse.setLastPage(true);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(leaveLogsResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAuditLogs()).hasSize(1);
            assertThat(response.getBody().getAuditLogs().get(0).getObjectType())
                    .isEqualTo(AuditObjectType.LEAVE_REQUEST);
        }

        @Test
        @DisplayName("성공: 페이지네이션 정보가 올바르게 반환된다")
        void getAllLogs_PaginationInfo() {
            // given
            Integer page = 2;
            Integer size = 5;
            String objectType = "all";

            AuditLogListResponse paginatedResponse = new AuditLogListResponse();
            paginatedResponse.setPage(2);
            paginatedResponse.setSize(5);
            paginatedResponse.setAuditLogs(List.of(mockDetailResponse));
            paginatedResponse.setTotalPage(3);
            paginatedResponse.setTotalElement(15L);
            paginatedResponse.setFirstPage(false);
            paginatedResponse.setLastPage(false);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(paginatedResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getPage()).isEqualTo(2);
            assertThat(response.getBody().getSize()).isEqualTo(5);
            assertThat(response.getBody().getTotalPage()).isEqualTo(3);
            assertThat(response.getBody().getTotalElement()).isEqualTo(15L);
            assertThat(response.getBody().getFirstPage()).isFalse();
            assertThat(response.getBody().getLastPage()).isFalse();
        }

        @Test
        @DisplayName("성공: 첫 페이지 조회 시 firstPage가 true이다")
        void getAllLogs_FirstPage() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getFirstPage()).isTrue();
        }

        @Test
        @DisplayName("성공: 마지막 페이지 조회 시 lastPage가 true이다")
        void getAllLogs_LastPage() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(mockListResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getLastPage()).isTrue();
        }

        @Test
        @DisplayName("성공: 최대 페이지 크기로 조회한다")
        void getAllLogs_MaxSize() {
            // given
            Integer page = 1;
            Integer size = 20;
            String objectType = "all";

            AuditLogListResponse maxSizeResponse = new AuditLogListResponse();
            maxSizeResponse.setPage(1);
            maxSizeResponse.setSize(20);
            maxSizeResponse.setAuditLogs(List.of(mockDetailResponse, mockDetailResponse2));
            maxSizeResponse.setTotalPage(1);
            maxSizeResponse.setTotalElement(2L);
            maxSizeResponse.setFirstPage(true);
            maxSizeResponse.setLastPage(true);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(maxSizeResponse);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getSize()).isEqualTo(20);
        }

        @Test
        @DisplayName("성공: 시스템 로그(actorId가 null)도 정상 조회된다")
        void getAllLogs_WithSystemLog() {
            // given
            Integer page = 1;
            Integer size = 10;
            String objectType = "all";

            AuditLogDetailResponse systemLogResponse = new AuditLogDetailResponse();
            systemLogResponse.setId(3L);
            systemLogResponse.setAction(Action.AUDIT_LOG_PURGED);
            systemLogResponse.setUserId(null);
            systemLogResponse.setUsername("관리자");
            systemLogResponse.setEmployeeNo(null);
            systemLogResponse.setObjectId(100L);
            systemLogResponse.setObjectType(AuditObjectType.USER);
            systemLogResponse.setSummary("시스템에 의해 로그가 정리되었습니다.");
            systemLogResponse.setOccurredAt(LocalDateTime.now());

            AuditLogListResponse responseWithSystemLog = new AuditLogListResponse();
            responseWithSystemLog.setPage(1);
            responseWithSystemLog.setSize(10);
            responseWithSystemLog.setAuditLogs(List.of(mockDetailResponse, systemLogResponse));
            responseWithSystemLog.setTotalPage(1);
            responseWithSystemLog.setTotalElement(2L);
            responseWithSystemLog.setFirstPage(true);
            responseWithSystemLog.setLastPage(true);

            given(auditLogService.getAllLogs(page, size, objectType))
                    .willReturn(responseWithSystemLog);

            // when
            ResponseEntity<AuditLogListResponse> response =
                    auditLogController.getAllLogs(page, size, objectType);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getAuditLogs()).hasSize(2);

            AuditLogDetailResponse systemLog = response.getBody().getAuditLogs().stream()
                    .filter(log -> log.getAction() == Action.AUDIT_LOG_PURGED)
                    .findFirst()
                    .orElse(null);
            assertThat(systemLog).isNotNull();
            assertThat(systemLog.getUsername()).isEqualTo("관리자");
            assertThat(systemLog.getUserId()).isNull();
        }
    }
}

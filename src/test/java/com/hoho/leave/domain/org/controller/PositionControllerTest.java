package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.request.PositionUpdateRequest;
import com.hoho.leave.domain.org.dto.response.PositionDetailResponse;
import com.hoho.leave.domain.org.dto.response.PositionListResponse;
import com.hoho.leave.domain.org.service.PositionService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("PositionController 테스트")
class PositionControllerTest {

    @InjectMocks
    private PositionController positionController;

    @Mock
    private PositionService positionService;

    private PositionDetailResponse mockDetailResponse;
    private PositionDetailResponse mockDetailResponse2;
    private PositionListResponse mockListResponse;
    private PositionCreateRequest createRequest;
    private PositionUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Mock PositionDetailResponse 생성
        mockDetailResponse = new PositionDetailResponse();
        mockDetailResponse.setId(1L);
        mockDetailResponse.setPositionName("팀장");
        mockDetailResponse.setOrderNo(1);

        mockDetailResponse2 = new PositionDetailResponse();
        mockDetailResponse2.setId(2L);
        mockDetailResponse2.setPositionName("파트장");
        mockDetailResponse2.setOrderNo(2);

        // Mock PositionListResponse 생성
        mockListResponse = new PositionListResponse();
        mockListResponse.setPage(1);
        mockListResponse.setSize(5);
        mockListResponse.setPositions(List.of(mockDetailResponse, mockDetailResponse2));
        mockListResponse.setTotalPage(1);
        mockListResponse.setTotalElement(2L);
        mockListResponse.setFirstPage(true);
        mockListResponse.setLastPage(true);

        // Request 객체 생성
        createRequest = new PositionCreateRequest();
        createRequest.setPositionName("팀장");
        createRequest.setOrderNo(1);

        updateRequest = new PositionUpdateRequest();
        updateRequest.setPositionName("수정된 팀장");
        updateRequest.setOrderNo(2);
    }

    @Nested
    @DisplayName("직책 생성")
    class CreatePosition {

        @Test
        @DisplayName("성공: 새로운 직책을 생성한다")
        void createPosition_Success() {
            // given
            doNothing().when(positionService).createPosition(createRequest);

            // when
            ResponseEntity<?> response = positionController.createPosition(createRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직책 등록 성공");
            verify(positionService).createPosition(createRequest);
        }
    }

    @Nested
    @DisplayName("직책 수정")
    class UpdatePosition {

        @Test
        @DisplayName("성공: 직책 정보를 수정한다")
        void updatePosition_Success() {
            // given
            Long positionId = 1L;
            doNothing().when(positionService).updatePosition(positionId, updateRequest);

            // when
            ResponseEntity<?> response = positionController.updatePosition(positionId, updateRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직책 수정 성공");
            verify(positionService).updatePosition(positionId, updateRequest);
        }
    }

    @Nested
    @DisplayName("직책 삭제")
    class DeletePosition {

        @Test
        @DisplayName("성공: 직책을 삭제한다")
        void deletePosition_Success() {
            // given
            Long positionId = 1L;
            doNothing().when(positionService).deletePosition(positionId);

            // when
            ResponseEntity<?> response = positionController.deletePosition(positionId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직책 삭제 성공");
            verify(positionService).deletePosition(positionId);
        }
    }

    @Nested
    @DisplayName("직책 단건 조회")
    class GetPosition {

        @Test
        @DisplayName("성공: 특정 직책을 조회한다")
        void getPosition_Success() {
            // given
            Long positionId = 1L;
            given(positionService.getPosition(positionId)).willReturn(mockDetailResponse);

            // when
            ResponseEntity<PositionDetailResponse> response = positionController.getPosition(positionId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getPositionName()).isEqualTo("팀장");
            assertThat(response.getBody().getOrderNo()).isEqualTo(1);
            verify(positionService).getPosition(positionId);
        }
    }

    @Nested
    @DisplayName("직책 목록 조회")
    class GetAllPositions {

        @Test
        @DisplayName("성공: 전체 직책 목록을 조회한다")
        void getAllPositions_Success() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(positionService.getAllPositions(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<PositionListResponse> response = positionController.getAllPositions(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getPositions()).hasSize(2);
            assertThat(response.getBody().getPage()).isEqualTo(1);
            verify(positionService).getAllPositions(size, page);
        }

        @Test
        @DisplayName("성공: 빈 직책 목록을 조회한다")
        void getAllPositions_EmptyList() {
            // given
            Integer size = 5;
            Integer page = 1;

            PositionListResponse emptyResponse = new PositionListResponse();
            emptyResponse.setPage(1);
            emptyResponse.setSize(5);
            emptyResponse.setPositions(List.of());
            emptyResponse.setTotalPage(0);
            emptyResponse.setTotalElement(0L);
            emptyResponse.setFirstPage(true);
            emptyResponse.setLastPage(true);

            given(positionService.getAllPositions(size, page)).willReturn(emptyResponse);

            // when
            ResponseEntity<PositionListResponse> response = positionController.getAllPositions(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getPositions()).isEmpty();
            assertThat(response.getBody().getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지네이션 정보가 올바르게 반환된다")
        void getAllPositions_PaginationInfo() {
            // given
            Integer size = 5;
            Integer page = 2;

            PositionListResponse paginatedResponse = new PositionListResponse();
            paginatedResponse.setPage(2);
            paginatedResponse.setSize(5);
            paginatedResponse.setPositions(List.of(mockDetailResponse));
            paginatedResponse.setTotalPage(3);
            paginatedResponse.setTotalElement(15L);
            paginatedResponse.setFirstPage(false);
            paginatedResponse.setLastPage(false);

            given(positionService.getAllPositions(size, page)).willReturn(paginatedResponse);

            // when
            ResponseEntity<PositionListResponse> response = positionController.getAllPositions(size, page);

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
        void getAllPositions_FirstPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(positionService.getAllPositions(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<PositionListResponse> response = positionController.getAllPositions(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getFirstPage()).isTrue();
        }

        @Test
        @DisplayName("성공: 마지막 페이지 조회 시 lastPage가 true이다")
        void getAllPositions_LastPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(positionService.getAllPositions(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<PositionListResponse> response = positionController.getAllPositions(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getLastPage()).isTrue();
        }
    }
}

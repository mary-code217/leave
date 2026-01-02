package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import com.hoho.leave.domain.org.dto.response.GradeListResponse;
import com.hoho.leave.domain.org.service.GradeService;
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
@DisplayName("GradeController 테스트")
class GradeControllerTest {

    @InjectMocks
    private GradeController gradeController;

    @Mock
    private GradeService gradeService;

    private GradeDetailResponse mockDetailResponse;
    private GradeDetailResponse mockDetailResponse2;
    private GradeListResponse mockListResponse;
    private GradeCreateRequest createRequest;
    private GradeUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Mock GradeDetailResponse 생성
        mockDetailResponse = new GradeDetailResponse();
        mockDetailResponse.setId(1L);
        mockDetailResponse.setGradeName("과장");
        mockDetailResponse.setOrderNo(1);

        mockDetailResponse2 = new GradeDetailResponse();
        mockDetailResponse2.setId(2L);
        mockDetailResponse2.setGradeName("대리");
        mockDetailResponse2.setOrderNo(2);

        // Mock GradeListResponse 생성
        mockListResponse = new GradeListResponse();
        mockListResponse.setPage(1);
        mockListResponse.setSize(5);
        mockListResponse.setGrades(List.of(mockDetailResponse, mockDetailResponse2));
        mockListResponse.setTotalPage(1);
        mockListResponse.setTotalElement(2L);
        mockListResponse.setFirstPage(true);
        mockListResponse.setLastPage(true);

        // Request 객체 생성
        createRequest = new GradeCreateRequest();
        createRequest.setGradeName("과장");
        createRequest.setOrderNo(1);

        updateRequest = new GradeUpdateRequest();
        updateRequest.setGradeName("수정된 과장");
        updateRequest.setOrderNo(2);
    }

    @Nested
    @DisplayName("직급 생성")
    class CreateGrade {

        @Test
        @DisplayName("성공: 새로운 직급을 생성한다")
        void createGrade_Success() {
            // given
            doNothing().when(gradeService).createGrade(createRequest);

            // when
            ResponseEntity<?> response = gradeController.createGrade(createRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직급 등록 성공");
            verify(gradeService).createGrade(createRequest);
        }
    }

    @Nested
    @DisplayName("직급 수정")
    class UpdateGrade {

        @Test
        @DisplayName("성공: 직급 정보를 수정한다")
        void updateGrade_Success() {
            // given
            Long gradeId = 1L;
            doNothing().when(gradeService).updateGrade(gradeId, updateRequest);

            // when
            ResponseEntity<?> response = gradeController.updateGrade(gradeId, updateRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직급 수정 성공");
            verify(gradeService).updateGrade(gradeId, updateRequest);
        }
    }

    @Nested
    @DisplayName("직급 삭제")
    class DeleteGrade {

        @Test
        @DisplayName("성공: 직급을 삭제한다")
        void deleteGrade_Success() {
            // given
            Long gradeId = 1L;
            doNothing().when(gradeService).deleteGrade(gradeId);

            // when
            ResponseEntity<?> response = gradeController.deleteGrade(gradeId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("직급 삭제 성공");
            verify(gradeService).deleteGrade(gradeId);
        }
    }

    @Nested
    @DisplayName("직급 단건 조회")
    class GetGrade {

        @Test
        @DisplayName("성공: 특정 직급을 조회한다")
        void getGrade_Success() {
            // given
            Long gradeId = 1L;
            given(gradeService.getGrade(gradeId)).willReturn(mockDetailResponse);

            // when
            ResponseEntity<GradeDetailResponse> response = gradeController.getGrade(gradeId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getId()).isEqualTo(1L);
            assertThat(response.getBody().getGradeName()).isEqualTo("과장");
            assertThat(response.getBody().getOrderNo()).isEqualTo(1);
            verify(gradeService).getGrade(gradeId);
        }
    }

    @Nested
    @DisplayName("직급 목록 조회")
    class GetAllGrades {

        @Test
        @DisplayName("성공: 전체 직급 목록을 조회한다")
        void getAllGrades_Success() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(gradeService.getAllGrades(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<GradeListResponse> response = gradeController.getAllGrades(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getGrades()).hasSize(2);
            assertThat(response.getBody().getPage()).isEqualTo(1);
            verify(gradeService).getAllGrades(size, page);
        }

        @Test
        @DisplayName("성공: 빈 직급 목록을 조회한다")
        void getAllGrades_EmptyList() {
            // given
            Integer size = 5;
            Integer page = 1;

            GradeListResponse emptyResponse = new GradeListResponse();
            emptyResponse.setPage(1);
            emptyResponse.setSize(5);
            emptyResponse.setGrades(List.of());
            emptyResponse.setTotalPage(0);
            emptyResponse.setTotalElement(0L);
            emptyResponse.setFirstPage(true);
            emptyResponse.setLastPage(true);

            given(gradeService.getAllGrades(size, page)).willReturn(emptyResponse);

            // when
            ResponseEntity<GradeListResponse> response = gradeController.getAllGrades(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getGrades()).isEmpty();
            assertThat(response.getBody().getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지네이션 정보가 올바르게 반환된다")
        void getAllGrades_PaginationInfo() {
            // given
            Integer size = 5;
            Integer page = 2;

            GradeListResponse paginatedResponse = new GradeListResponse();
            paginatedResponse.setPage(2);
            paginatedResponse.setSize(5);
            paginatedResponse.setGrades(List.of(mockDetailResponse));
            paginatedResponse.setTotalPage(3);
            paginatedResponse.setTotalElement(15L);
            paginatedResponse.setFirstPage(false);
            paginatedResponse.setLastPage(false);

            given(gradeService.getAllGrades(size, page)).willReturn(paginatedResponse);

            // when
            ResponseEntity<GradeListResponse> response = gradeController.getAllGrades(size, page);

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
        void getAllGrades_FirstPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(gradeService.getAllGrades(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<GradeListResponse> response = gradeController.getAllGrades(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getFirstPage()).isTrue();
        }

        @Test
        @DisplayName("성공: 마지막 페이지 조회 시 lastPage가 true이다")
        void getAllGrades_LastPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(gradeService.getAllGrades(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<GradeListResponse> response = gradeController.getAllGrades(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getLastPage()).isTrue();
        }
    }
}

package com.hoho.leave.domain.org.controller;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.dto.response.TeamListResponse;
import com.hoho.leave.domain.org.service.TeamService;
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
@DisplayName("TeamController 테스트")
class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @Mock
    private TeamService teamService;

    private TeamDetailResponse mockDetailResponse;
    private TeamDetailResponse mockDetailResponse2;
    private TeamListResponse mockListResponse;
    private TeamCreateRequest createRequest;
    private TeamUpdateRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Mock TeamDetailResponse 생성 (루트 팀)
        mockDetailResponse = new TeamDetailResponse();
        mockDetailResponse.setTeamId(1L);
        mockDetailResponse.setTeamName("개발본부");
        mockDetailResponse.setOrderNo(1);
        mockDetailResponse.setParentId(0L);
        mockDetailResponse.setParentName("");
        mockDetailResponse.setUserCount(10L);
        mockDetailResponse.setChildrenCount(2L);

        // Mock TeamDetailResponse 생성 (하위 팀)
        mockDetailResponse2 = new TeamDetailResponse();
        mockDetailResponse2.setTeamId(2L);
        mockDetailResponse2.setTeamName("백엔드팀");
        mockDetailResponse2.setOrderNo(1);
        mockDetailResponse2.setParentId(1L);
        mockDetailResponse2.setParentName("개발본부");
        mockDetailResponse2.setUserCount(5L);
        mockDetailResponse2.setChildrenCount(0L);

        // Mock TeamListResponse 생성
        mockListResponse = new TeamListResponse();
        mockListResponse.setPage(1);
        mockListResponse.setSize(5);
        mockListResponse.setTeams(List.of(mockDetailResponse, mockDetailResponse2));
        mockListResponse.setTotalPage(1);
        mockListResponse.setTotalElement(2L);
        mockListResponse.setFirstPage(true);
        mockListResponse.setLastPage(true);

        // Request 객체 생성
        createRequest = new TeamCreateRequest();
        createRequest.setTeamName("개발본부");
        createRequest.setOrderNo(1);
        createRequest.setParentId(null);

        updateRequest = new TeamUpdateRequest();
        updateRequest.setTeamName("수정된 개발본부");
        updateRequest.setOrderNo(2);
        updateRequest.setParentId(null);
    }

    @Nested
    @DisplayName("팀 생성")
    class CreateTeam {

        @Test
        @DisplayName("성공: 새로운 팀을 생성한다")
        void createTeam_Success() {
            // given
            doNothing().when(teamService).createTeam(createRequest);

            // when
            ResponseEntity<?> response = teamController.createTeam(createRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("부서 등록 성공");
            verify(teamService).createTeam(createRequest);
        }

        @Test
        @DisplayName("성공: 하위 팀을 생성한다")
        void createTeam_WithParent_Success() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("백엔드팀");
            childRequest.setOrderNo(1);
            childRequest.setParentId(1L);

            doNothing().when(teamService).createTeam(childRequest);

            // when
            ResponseEntity<?> response = teamController.createTeam(childRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("부서 등록 성공");
            verify(teamService).createTeam(childRequest);
        }
    }

    @Nested
    @DisplayName("팀 수정")
    class UpdateTeam {

        @Test
        @DisplayName("성공: 팀 정보를 수정한다")
        void updateTeam_Success() {
            // given
            Long teamId = 1L;
            doNothing().when(teamService).updateTeam(teamId, updateRequest);

            // when
            ResponseEntity<?> response = teamController.updateTeam(teamId, updateRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("부서 변경 성공");
            verify(teamService).updateTeam(teamId, updateRequest);
        }

        @Test
        @DisplayName("성공: 상위 팀을 변경한다")
        void updateTeam_ChangeParent_Success() {
            // given
            Long teamId = 2L;
            TeamUpdateRequest changeParentRequest = new TeamUpdateRequest();
            changeParentRequest.setTeamName("백엔드팀");
            changeParentRequest.setParentId(3L);
            changeParentRequest.setOrderNo(1);

            doNothing().when(teamService).updateTeam(teamId, changeParentRequest);

            // when
            ResponseEntity<?> response = teamController.updateTeam(teamId, changeParentRequest);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("부서 변경 성공");
            verify(teamService).updateTeam(teamId, changeParentRequest);
        }
    }

    @Nested
    @DisplayName("팀 삭제")
    class DeleteTeam {

        @Test
        @DisplayName("성공: 팀을 삭제한다")
        void deleteTeam_Success() {
            // given
            Long teamId = 1L;
            doNothing().when(teamService).deleteTeam(teamId);

            // when
            ResponseEntity<?> response = teamController.deleteTeam(teamId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo("부서 삭제 성공");
            verify(teamService).deleteTeam(teamId);
        }
    }

    @Nested
    @DisplayName("팀 단건 조회")
    class GetTeam {

        @Test
        @DisplayName("성공: 루트 팀을 조회한다")
        void getTeam_RootTeam_Success() {
            // given
            Long teamId = 1L;
            given(teamService.getTeam(teamId)).willReturn(mockDetailResponse);

            // when
            ResponseEntity<TeamDetailResponse> response = teamController.getTeam(teamId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTeamId()).isEqualTo(1L);
            assertThat(response.getBody().getTeamName()).isEqualTo("개발본부");
            assertThat(response.getBody().getParentId()).isEqualTo(0L);
            assertThat(response.getBody().getParentName()).isEmpty();
            assertThat(response.getBody().getUserCount()).isEqualTo(10L);
            assertThat(response.getBody().getChildrenCount()).isEqualTo(2L);
            verify(teamService).getTeam(teamId);
        }

        @Test
        @DisplayName("성공: 하위 팀을 조회한다")
        void getTeam_ChildTeam_Success() {
            // given
            Long teamId = 2L;
            given(teamService.getTeam(teamId)).willReturn(mockDetailResponse2);

            // when
            ResponseEntity<TeamDetailResponse> response = teamController.getTeam(teamId);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTeamId()).isEqualTo(2L);
            assertThat(response.getBody().getTeamName()).isEqualTo("백엔드팀");
            assertThat(response.getBody().getParentId()).isEqualTo(1L);
            assertThat(response.getBody().getParentName()).isEqualTo("개발본부");
            verify(teamService).getTeam(teamId);
        }
    }

    @Nested
    @DisplayName("팀 목록 조회")
    class GetAllTeams {

        @Test
        @DisplayName("성공: 전체 팀 목록을 조회한다")
        void getAllTeams_Success() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(teamService.getAllTeams(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<TeamListResponse> response = teamController.getAllTeams(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTeams()).hasSize(2);
            assertThat(response.getBody().getPage()).isEqualTo(1);
            verify(teamService).getAllTeams(size, page);
        }

        @Test
        @DisplayName("성공: 빈 팀 목록을 조회한다")
        void getAllTeams_EmptyList() {
            // given
            Integer size = 5;
            Integer page = 1;

            TeamListResponse emptyResponse = new TeamListResponse();
            emptyResponse.setPage(1);
            emptyResponse.setSize(5);
            emptyResponse.setTeams(List.of());
            emptyResponse.setTotalPage(0);
            emptyResponse.setTotalElement(0L);
            emptyResponse.setFirstPage(true);
            emptyResponse.setLastPage(true);

            given(teamService.getAllTeams(size, page)).willReturn(emptyResponse);

            // when
            ResponseEntity<TeamListResponse> response = teamController.getAllTeams(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getTeams()).isEmpty();
            assertThat(response.getBody().getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지네이션 정보가 올바르게 반환된다")
        void getAllTeams_PaginationInfo() {
            // given
            Integer size = 5;
            Integer page = 2;

            TeamListResponse paginatedResponse = new TeamListResponse();
            paginatedResponse.setPage(2);
            paginatedResponse.setSize(5);
            paginatedResponse.setTeams(List.of(mockDetailResponse));
            paginatedResponse.setTotalPage(3);
            paginatedResponse.setTotalElement(15L);
            paginatedResponse.setFirstPage(false);
            paginatedResponse.setLastPage(false);

            given(teamService.getAllTeams(size, page)).willReturn(paginatedResponse);

            // when
            ResponseEntity<TeamListResponse> response = teamController.getAllTeams(size, page);

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
        void getAllTeams_FirstPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(teamService.getAllTeams(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<TeamListResponse> response = teamController.getAllTeams(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getFirstPage()).isTrue();
        }

        @Test
        @DisplayName("성공: 마지막 페이지 조회 시 lastPage가 true이다")
        void getAllTeams_LastPage() {
            // given
            Integer size = 5;
            Integer page = 1;
            given(teamService.getAllTeams(size, page)).willReturn(mockListResponse);

            // when
            ResponseEntity<TeamListResponse> response = teamController.getAllTeams(size, page);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().getLastPage()).isTrue();
        }
    }
}

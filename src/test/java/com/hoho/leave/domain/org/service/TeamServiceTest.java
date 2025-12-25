package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.dto.request.TeamUpdateRequest;
import com.hoho.leave.domain.org.dto.response.TeamDetailResponse;
import com.hoho.leave.domain.org.dto.response.TeamListResponse;
import com.hoho.leave.domain.org.entity.Team;
import com.hoho.leave.domain.org.repository.TeamRepository;
import com.hoho.leave.domain.user.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamService 테스트")
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private UserRepository userRepository;

    private TeamCreateRequest createRequest;
    private TeamUpdateRequest updateRequest;
    private Team mockTeam;
    private Team mockParentTeam;

    @BeforeEach
    void setUp() {
        // 목 데이터 설정
        createRequest = new TeamCreateRequest();
        createRequest.setTeamName("개발팀");
        createRequest.setOrderNo(1);

        updateRequest = new TeamUpdateRequest();
        updateRequest.setTeamName("백엔드팀");
        updateRequest.setOrderNo(2);

        mockTeam = Team.createTeam(createRequest);
        ReflectionTestUtils.setField(mockTeam, "id", 1L);

        TeamCreateRequest parentRequest = new TeamCreateRequest();
        parentRequest.setTeamName("IT본부");
        parentRequest.setOrderNo(0);
        mockParentTeam = Team.createTeam(parentRequest);
        ReflectionTestUtils.setField(mockParentTeam, "id", 100L);
    }

    @Nested
    @DisplayName("팀 생성")
    class CreateTeam {

        @Test
        @DisplayName("성공: 루트 팀을 정상적으로 생성한다")
        void createRootTeam_Success() {
            // given
            given(teamRepository.existsByTeamName("개발팀")).willReturn(false);
            given(teamRepository.save(any(Team.class))).willReturn(mockTeam);

            // when
            teamService.createTeam(createRequest);

            // then
            verify(teamRepository).existsByTeamName("개발팀");
            verify(teamRepository).save(any(Team.class));
        }

        @Test
        @DisplayName("성공: 하위 팀을 정상적으로 생성한다")
        void createChildTeam_Success() {
            // given
            createRequest.setParentId(100L);
            given(teamRepository.existsByTeamName("개발팀")).willReturn(false);
            given(teamRepository.findById(100L)).willReturn(Optional.of(mockParentTeam));
            given(teamRepository.save(any(Team.class))).willReturn(mockTeam);

            // when
            teamService.createTeam(createRequest);

            // then
            verify(teamRepository).existsByTeamName("개발팀");
            verify(teamRepository).findById(100L);
            verify(teamRepository).save(any(Team.class));
        }

        @Test
        @DisplayName("실패: 중복된 팀명으로 생성 시 예외 발생")
        void createTeam_DuplicateName_ThrowsException() {
            // given
            given(teamRepository.existsByTeamName("개발팀")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamService.createTeam(createRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate TeamName");

            verify(teamRepository).existsByTeamName("개발팀");
            verify(teamRepository, never()).save(any(Team.class));
        }

        @Test
        @DisplayName("실패: 존재하지 않는 상위 팀으로 생성 시 예외 발생")
        void createTeam_ParentNotFound_ThrowsException() {
            // given
            createRequest.setParentId(999L);
            given(teamRepository.existsByTeamName("개발팀")).willReturn(false);
            given(teamRepository.findById(999L)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamService.createTeam(createRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Team");
        }
    }

    @Nested
    @DisplayName("팀 수정")
    class UpdateTeam {

        @Test
        @DisplayName("성공: 정상적으로 팀을 수정한다")
        void updateTeam_Success() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByTeamNameAndIdNot("백엔드팀", teamId)).willReturn(false);

            // when
            teamService.updateTeam(teamId, updateRequest);

            // then
            verify(teamRepository).findById(teamId);
            verify(teamRepository).existsByTeamNameAndIdNot("백엔드팀", teamId);
            assertThat(mockTeam.getTeamName()).isEqualTo("백엔드팀");
            assertThat(mockTeam.getOrderNo()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: 상위 팀을 변경한다")
        void updateTeam_ChangeParent_Success() {
            // given
            Long teamId = 1L;
            updateRequest.setParentId(100L);

            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByTeamNameAndIdNot("백엔드팀", teamId)).willReturn(false);
            given(teamRepository.findById(100L)).willReturn(Optional.of(mockParentTeam));

            // when
            teamService.updateTeam(teamId, updateRequest);

            // then
            verify(teamRepository, times(2)).findById(any());
            assertThat(mockTeam.getParent()).isEqualTo(mockParentTeam);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 팀 수정 시 예외 발생")
        void updateTeam_NotFound_ThrowsException() {
            // given
            Long teamId = 999L;
            given(teamRepository.findById(teamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamService.updateTeam(teamId, updateRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Team");
        }

        @Test
        @DisplayName("실패: 중복된 팀명으로 수정 시 예외 발생")
        void updateTeam_DuplicateName_ThrowsException() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByTeamNameAndIdNot("백엔드팀", teamId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamService.updateTeam(teamId, updateRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate TeamName");
        }

        @Test
        @DisplayName("실패: 자기 자신을 상위 부서로 지정 시 예외 발생")
        void updateTeam_SelfParent_ThrowsException() {
            // given
            Long teamId = 1L;
            updateRequest.setParentId(1L);

            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByTeamNameAndIdNot("백엔드팀", teamId)).willReturn(false);

            // when & then
            assertThatThrownBy(() -> teamService.updateTeam(teamId, updateRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("자기 자신을 상위 부서로 지정할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("팀 삭제")
    class DeleteTeam {

        @Test
        @DisplayName("성공: 정상적으로 팀을 삭제한다")
        void deleteTeam_Success() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByParentId(teamId)).willReturn(false);
            given(userRepository.existsByTeamId(teamId)).willReturn(false);

            // when
            teamService.deleteTeam(teamId);

            // then
            verify(teamRepository).findById(teamId);
            verify(teamRepository).existsByParentId(teamId);
            verify(userRepository).existsByTeamId(teamId);
            verify(teamRepository).delete(mockTeam);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 팀 삭제 시 예외 발생")
        void deleteTeam_NotFound_ThrowsException() {
            // given
            Long teamId = 999L;
            given(teamRepository.findById(teamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamService.deleteTeam(teamId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Team");
        }

        @Test
        @DisplayName("실패: 하위 부서가 있는 팀 삭제 시 예외 발생")
        void deleteTeam_ExistChild_ThrowsException() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByParentId(teamId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamService.deleteTeam(teamId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("하위 부서가 존재합니다");

            verify(teamRepository, never()).delete(any(Team.class));
        }

        @Test
        @DisplayName("실패: 소속 사용자가 있는 팀 삭제 시 예외 발생")
        void deleteTeam_ExistUser_ThrowsException() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(teamRepository.existsByParentId(teamId)).willReturn(false);
            given(userRepository.existsByTeamId(teamId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> teamService.deleteTeam(teamId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("소속 사용자가 존재합니다");

            verify(teamRepository, never()).delete(any(Team.class));
        }
    }

    @Nested
    @DisplayName("팀 조회")
    class GetTeam {

        @Test
        @DisplayName("성공: 특정 팀을 조회한다")
        void getTeam_Success() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));
            given(userRepository.countByTeamId(teamId)).willReturn(5L);
            given(teamRepository.countByParentId(teamId)).willReturn(2L);

            // when
            TeamDetailResponse response = teamService.getTeam(teamId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getTeamName()).isEqualTo("개발팀");
            assertThat(response.getOrderNo()).isEqualTo(1);
            assertThat(response.getUserCount()).isEqualTo(5L);
            assertThat(response.getChildrenCount()).isEqualTo(2L);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 팀 조회 시 예외 발생")
        void getTeam_NotFound_ThrowsException() {
            // given
            Long teamId = 999L;
            given(teamRepository.findById(teamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamService.getTeam(teamId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Team");
        }
    }

    @Nested
    @DisplayName("팀 목록 조회")
    class GetAllTeams {

        @Test
        @DisplayName("성공: 전체 팀 목록을 페이지네이션으로 조회한다")
        void getAllTeams_Success() {
            // given
            TeamCreateRequest request2 = new TeamCreateRequest();
            request2.setTeamName("프론트엔드팀");
            request2.setOrderNo(2);
            Team team2 = Team.createTeam(request2);
            ReflectionTestUtils.setField(team2, "id", 2L);

            List<Team> teams = List.of(mockTeam, team2);
            Page<Team> teamPage = new PageImpl<>(teams);

            given(teamRepository.findAll(any(Pageable.class))).willReturn(teamPage);
            given(userRepository.countByTeamIds(anyList())).willReturn(List.of(
                    new Object[]{1L, 5L},
                    new Object[]{2L, 3L}
            ));
            given(teamRepository.countChildrenByTeamIds(anyList())).willReturn(List.of(
                    new Object[]{1L, 1L},
                    new Object[]{2L, 0L}
            ));

            // when
            TeamListResponse response = teamService.getAllTeams(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getTeams()).hasSize(2);
            verify(teamRepository).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 빈 목록 조회 시 빈 응답 반환")
        void getAllTeams_EmptyList() {
            // given
            Page<Team> emptyPage = new PageImpl<>(List.of());
            given(teamRepository.findAll(any(Pageable.class))).willReturn(emptyPage);
            given(userRepository.countByTeamIds(anyList())).willReturn(List.of());
            given(teamRepository.countChildrenByTeamIds(anyList())).willReturn(List.of());

            // when
            TeamListResponse response = teamService.getAllTeams(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getTeams()).isEmpty();
        }
    }

    @Nested
    @DisplayName("팀 엔티티 조회")
    class GetTeamEntity {

        @Test
        @DisplayName("성공: 팀 엔티티를 조회한다")
        void getTeamEntity_Success() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));

            // when
            Team result = teamService.getTeamEntity(teamId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getTeamName()).isEqualTo("개발팀");
        }

        @Test
        @DisplayName("실패: 존재하지 않는 팀 엔티티 조회 시 예외 발생")
        void getTeamEntity_NotFound_ThrowsException() {
            // given
            Long teamId = 999L;
            given(teamRepository.findById(teamId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> teamService.getTeamEntity(teamId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Team");
        }
    }
}

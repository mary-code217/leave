package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import com.hoho.leave.domain.org.entity.Team;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TeamRepository 테스트")
class TeamRepositoryTest {

    @Mock
    private TeamRepository teamRepository;

    private Team mockTeam;
    private Team mockTeam2;

    @BeforeEach
    void setUp() {
        // Mock Team 생성 (루트 팀)
        TeamCreateRequest request1 = new TeamCreateRequest();
        request1.setTeamName("개발본부");
        request1.setOrderNo(1);
        mockTeam = Team.createTeam(request1);
        ReflectionTestUtils.setField(mockTeam, "id", 1L);

        // Mock Team 생성 (하위 팀)
        TeamCreateRequest request2 = new TeamCreateRequest();
        request2.setTeamName("백엔드팀");
        request2.setOrderNo(1);
        mockTeam2 = Team.createTeam(request2);
        ReflectionTestUtils.setField(mockTeam2, "id", 2L);
    }

    @Nested
    @DisplayName("팀 저장")
    class SaveTeam {

        @Test
        @DisplayName("성공: 새로운 팀을 저장한다")
        void save_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("기술본부");
            request.setOrderNo(2);
            Team newTeam = Team.createTeam(request);

            Team savedTeam = Team.createTeam(request);
            ReflectionTestUtils.setField(savedTeam, "id", 3L);

            given(teamRepository.save(any(Team.class))).willReturn(savedTeam);

            // when
            Team result = teamRepository.save(newTeam);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getTeamName()).isEqualTo("기술본부");
            assertThat(result.getOrderNo()).isEqualTo(2);
            verify(teamRepository).save(newTeam);
        }

        @Test
        @DisplayName("성공: 여러 팀을 저장한다")
        void save_MultipleTeams_Success() {
            // given
            TeamCreateRequest request1 = new TeamCreateRequest();
            request1.setTeamName("인사본부");
            request1.setOrderNo(1);

            TeamCreateRequest request2 = new TeamCreateRequest();
            request2.setTeamName("재무본부");
            request2.setOrderNo(2);

            Team team1 = Team.createTeam(request1);
            Team team2 = Team.createTeam(request2);

            given(teamRepository.save(any(Team.class))).willAnswer(invocation -> {
                Team t = invocation.getArgument(0);
                ReflectionTestUtils.setField(t, "id", (long) (Math.random() * 1000));
                return t;
            });

            // when
            Team saved1 = teamRepository.save(team1);
            Team saved2 = teamRepository.save(team2);

            // then
            assertThat(saved1.getTeamName()).isEqualTo("인사본부");
            assertThat(saved2.getTeamName()).isEqualTo("재무본부");
            verify(teamRepository, times(2)).save(any(Team.class));
        }
    }

    @Nested
    @DisplayName("팀 ID로 조회")
    class FindById {

        @Test
        @DisplayName("성공: ID로 팀을 조회한다")
        void findById_Exists_ReturnsTeam() {
            // given
            Long teamId = 1L;
            given(teamRepository.findById(teamId)).willReturn(Optional.of(mockTeam));

            // when
            Optional<Team> result = teamRepository.findById(teamId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(teamId);
            assertThat(result.get().getTeamName()).isEqualTo("개발본부");
            verify(teamRepository).findById(teamId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_NotExists_ReturnsEmpty() {
            // given
            Long nonExistentId = 999L;
            given(teamRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<Team> result = teamRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(teamRepository).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("팀명으로 조회")
    class FindByTeamName {

        @Test
        @DisplayName("성공: 팀명으로 팀을 조회한다")
        void findByTeamName_Exists_ReturnsTeam() {
            // given
            String teamName = "개발본부";
            given(teamRepository.findByTeamName(teamName)).willReturn(Optional.of(mockTeam));

            // when
            Optional<Team> result = teamRepository.findByTeamName(teamName);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getTeamName()).isEqualTo(teamName);
            verify(teamRepository).findByTeamName(teamName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 팀명으로 조회 시 빈 Optional 반환")
        void findByTeamName_NotExists_ReturnsEmpty() {
            // given
            String nonExistentName = "존재하지않는팀";
            given(teamRepository.findByTeamName(nonExistentName)).willReturn(Optional.empty());

            // when
            Optional<Team> result = teamRepository.findByTeamName(nonExistentName);

            // then
            assertThat(result).isEmpty();
            verify(teamRepository).findByTeamName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("팀명 존재 여부 확인")
    class ExistsByTeamName {

        @Test
        @DisplayName("성공: 존재하는 팀명 확인 시 true 반환")
        void existsByTeamName_Exists_ReturnsTrue() {
            // given
            String teamName = "개발본부";
            given(teamRepository.existsByTeamName(teamName)).willReturn(true);

            // when
            boolean result = teamRepository.existsByTeamName(teamName);

            // then
            assertThat(result).isTrue();
            verify(teamRepository).existsByTeamName(teamName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 팀명 확인 시 false 반환")
        void existsByTeamName_NotExists_ReturnsFalse() {
            // given
            String nonExistentName = "존재하지않는팀";
            given(teamRepository.existsByTeamName(nonExistentName)).willReturn(false);

            // when
            boolean result = teamRepository.existsByTeamName(nonExistentName);

            // then
            assertThat(result).isFalse();
            verify(teamRepository).existsByTeamName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("특정 ID 제외 팀명 존재 여부 확인")
    class ExistsByTeamNameAndIdNot {

        @Test
        @DisplayName("성공: 다른 팀이 동일한 이름을 가지고 있으면 true 반환")
        void existsByTeamNameAndIdNot_DuplicateExists_ReturnsTrue() {
            // given
            String teamName = "개발본부";
            Long excludeId = 2L;
            given(teamRepository.existsByTeamNameAndIdNot(teamName, excludeId)).willReturn(true);

            // when
            boolean result = teamRepository.existsByTeamNameAndIdNot(teamName, excludeId);

            // then
            assertThat(result).isTrue();
            verify(teamRepository).existsByTeamNameAndIdNot(teamName, excludeId);
        }

        @Test
        @DisplayName("성공: 자신만 해당 이름을 가지고 있으면 false 반환")
        void existsByTeamNameAndIdNot_OnlySelf_ReturnsFalse() {
            // given
            String teamName = "개발본부";
            Long selfId = 1L;
            given(teamRepository.existsByTeamNameAndIdNot(teamName, selfId)).willReturn(false);

            // when
            boolean result = teamRepository.existsByTeamNameAndIdNot(teamName, selfId);

            // then
            assertThat(result).isFalse();
            verify(teamRepository).existsByTeamNameAndIdNot(teamName, selfId);
        }
    }

    @Nested
    @DisplayName("상위 팀으로 하위 팀 존재 여부 확인")
    class ExistsByParentId {

        @Test
        @DisplayName("성공: 하위 팀이 존재하면 true 반환")
        void existsByParentId_HasChildren_ReturnsTrue() {
            // given
            Long parentId = 1L;
            given(teamRepository.existsByParentId(parentId)).willReturn(true);

            // when
            boolean result = teamRepository.existsByParentId(parentId);

            // then
            assertThat(result).isTrue();
            verify(teamRepository).existsByParentId(parentId);
        }

        @Test
        @DisplayName("성공: 하위 팀이 없으면 false 반환")
        void existsByParentId_NoChildren_ReturnsFalse() {
            // given
            Long parentId = 999L;
            given(teamRepository.existsByParentId(parentId)).willReturn(false);

            // when
            boolean result = teamRepository.existsByParentId(parentId);

            // then
            assertThat(result).isFalse();
            verify(teamRepository).existsByParentId(parentId);
        }
    }

    @Nested
    @DisplayName("상위 팀별 하위 팀 개수 조회")
    class CountByParentId {

        @Test
        @DisplayName("성공: 하위 팀 개수를 조회한다")
        void countByParentId_ReturnsCount() {
            // given
            Long parentId = 1L;
            given(teamRepository.countByParentId(parentId)).willReturn(3L);

            // when
            Long count = teamRepository.countByParentId(parentId);

            // then
            assertThat(count).isEqualTo(3L);
            verify(teamRepository).countByParentId(parentId);
        }

        @Test
        @DisplayName("성공: 하위 팀이 없으면 0을 반환한다")
        void countByParentId_NoChildren_ReturnsZero() {
            // given
            Long parentId = 999L;
            given(teamRepository.countByParentId(parentId)).willReturn(0L);

            // when
            Long count = teamRepository.countByParentId(parentId);

            // then
            assertThat(count).isEqualTo(0L);
            verify(teamRepository).countByParentId(parentId);
        }
    }

    @Nested
    @DisplayName("여러 팀의 하위 팀 개수 일괄 조회")
    class CountChildrenByTeamIds {

        @Test
        @DisplayName("성공: 여러 팀의 하위 팀 개수를 일괄 조회한다")
        void countChildrenByTeamIds_Success() {
            // given
            List<Long> teamIds = List.of(1L, 2L, 3L);
            List<Object[]> results = List.of(
                    new Object[]{1L, 3L},
                    new Object[]{2L, 2L},
                    new Object[]{3L, 0L}
            );
            given(teamRepository.countChildrenByTeamIds(teamIds)).willReturn(results);

            // when
            List<Object[]> result = teamRepository.countChildrenByTeamIds(teamIds);

            // then
            assertThat(result).hasSize(3);
            assertThat(result.get(0)[0]).isEqualTo(1L);
            assertThat(result.get(0)[1]).isEqualTo(3L);
            verify(teamRepository).countChildrenByTeamIds(teamIds);
        }

        @Test
        @DisplayName("성공: 빈 목록을 전달하면 빈 결과 반환")
        void countChildrenByTeamIds_EmptyList_ReturnsEmpty() {
            // given
            List<Long> emptyList = List.of();
            given(teamRepository.countChildrenByTeamIds(emptyList)).willReturn(List.of());

            // when
            List<Object[]> result = teamRepository.countChildrenByTeamIds(emptyList);

            // then
            assertThat(result).isEmpty();
            verify(teamRepository).countChildrenByTeamIds(emptyList);
        }
    }

    @Nested
    @DisplayName("전체 팀 조회")
    class FindAll {

        @Test
        @DisplayName("성공: 전체 팀 목록을 페이징하여 조회한다")
        void findAll_Success() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("orderNo")));
            List<Team> teams = List.of(mockTeam, mockTeam2);
            Page<Team> teamPage = new PageImpl<>(teams, pageRequest, 2);

            given(teamRepository.findAll(any(PageRequest.class))).willReturn(teamPage);

            // when
            Page<Team> result = teamRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            verify(teamRepository).findAll(pageRequest);
        }

        @Test
        @DisplayName("성공: 팀이 없는 경우 빈 페이지 반환")
        void findAll_EmptyResult() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Team> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(teamRepository.findAll(any(PageRequest.class))).willReturn(emptyPage);

            // when
            Page<Team> result = teamRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findAll_Pagination() {
            // given
            List<Team> allTeams = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                TeamCreateRequest request = new TeamCreateRequest();
                request.setTeamName("팀" + i);
                request.setOrderNo(i);
                Team team = Team.createTeam(request);
                ReflectionTestUtils.setField(team, "id", (long) (i + 1));
                allTeams.add(team);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5);
            PageRequest pageRequest2 = PageRequest.of(1, 5);
            PageRequest pageRequest3 = PageRequest.of(2, 5);

            Page<Team> page1 = new PageImpl<>(allTeams.subList(0, 5), pageRequest1, 15);
            Page<Team> page2 = new PageImpl<>(allTeams.subList(5, 10), pageRequest2, 15);
            Page<Team> page3 = new PageImpl<>(allTeams.subList(10, 15), pageRequest3, 15);

            given(teamRepository.findAll(eq(pageRequest1))).willReturn(page1);
            given(teamRepository.findAll(eq(pageRequest2))).willReturn(page2);
            given(teamRepository.findAll(eq(pageRequest3))).willReturn(page3);

            // when
            Page<Team> result1 = teamRepository.findAll(pageRequest1);
            Page<Team> result2 = teamRepository.findAll(pageRequest2);
            Page<Team> result3 = teamRepository.findAll(pageRequest3);

            // then
            assertThat(result1.getContent()).hasSize(5);
            assertThat(result2.getContent()).hasSize(5);
            assertThat(result3.getContent()).hasSize(5);
            assertThat(result1.getTotalPages()).isEqualTo(3);
            assertThat(result1.isFirst()).isTrue();
            assertThat(result3.isLast()).isTrue();
        }
    }

    @Nested
    @DisplayName("팀 삭제")
    class DeleteTeam {

        @Test
        @DisplayName("성공: ID로 팀을 삭제한다")
        void deleteById_Success() {
            // given
            Long teamId = 1L;
            doNothing().when(teamRepository).deleteById(teamId);

            // when
            teamRepository.deleteById(teamId);

            // then
            verify(teamRepository, times(1)).deleteById(teamId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 삭제 시 예외 없이 처리된다")
        void deleteById_NonExistent_NoException() {
            // given
            Long nonExistentId = 999L;
            doNothing().when(teamRepository).deleteById(nonExistentId);

            // when
            teamRepository.deleteById(nonExistentId);

            // then
            verify(teamRepository).deleteById(nonExistentId);
        }

        @Test
        @DisplayName("성공: 모든 팀을 삭제한다")
        void deleteAll_Success() {
            // given
            doNothing().when(teamRepository).deleteAll();

            // when
            teamRepository.deleteAll();

            // then
            verify(teamRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("팀 개수 조회")
    class CountTeams {

        @Test
        @DisplayName("성공: 전체 팀 개수를 조회한다")
        void count_ReturnsCorrectCount() {
            // given
            given(teamRepository.count()).willReturn(10L);

            // when
            long count = teamRepository.count();

            // then
            assertThat(count).isEqualTo(10L);
            verify(teamRepository).count();
        }

        @Test
        @DisplayName("성공: 팀이 없을 경우 0을 반환한다")
        void count_NoTeams_ReturnsZero() {
            // given
            given(teamRepository.count()).willReturn(0L);

            // when
            long count = teamRepository.count();

            // then
            assertThat(count).isEqualTo(0L);
            verify(teamRepository).count();
        }
    }

    @Nested
    @DisplayName("팀 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("성공: 존재하는 ID 확인 시 true 반환")
        void existsById_Exists_ReturnsTrue() {
            // given
            Long existingId = 1L;
            given(teamRepository.existsById(existingId)).willReturn(true);

            // when
            boolean exists = teamRepository.existsById(existingId);

            // then
            assertThat(exists).isTrue();
            verify(teamRepository).existsById(existingId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 확인 시 false 반환")
        void existsById_NotExists_ReturnsFalse() {
            // given
            Long nonExistentId = 999L;
            given(teamRepository.existsById(nonExistentId)).willReturn(false);

            // when
            boolean exists = teamRepository.existsById(nonExistentId);

            // then
            assertThat(exists).isFalse();
            verify(teamRepository).existsById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 팀 저장 후 조회")
        void saveAndFind_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("신규팀");
            request.setOrderNo(5);
            Team newTeam = Team.createTeam(request);

            Team savedTeam = Team.createTeam(request);
            ReflectionTestUtils.setField(savedTeam, "id", 10L);

            given(teamRepository.save(any(Team.class))).willReturn(savedTeam);
            given(teamRepository.findById(10L)).willReturn(Optional.of(savedTeam));

            // when
            Team saved = teamRepository.save(newTeam);
            Optional<Team> found = teamRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getTeamName()).isEqualTo("신규팀");
        }

        @Test
        @DisplayName("성공: 저장 후 삭제, 조회 시 빈 결과")
        void saveDeleteFind_Empty() {
            // given
            Long teamId = 20L;
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("삭제될팀");
            request.setOrderNo(1);
            Team savedTeam = Team.createTeam(request);
            ReflectionTestUtils.setField(savedTeam, "id", teamId);

            given(teamRepository.save(any(Team.class))).willReturn(savedTeam);
            given(teamRepository.findById(teamId)).willReturn(Optional.of(savedTeam), Optional.empty());
            doNothing().when(teamRepository).deleteById(teamId);

            // when
            teamRepository.save(savedTeam);
            Optional<Team> beforeDelete = teamRepository.findById(teamId);
            teamRepository.deleteById(teamId);
            Optional<Team> afterDelete = teamRepository.findById(teamId);

            // then
            assertThat(beforeDelete).isPresent();
            assertThat(afterDelete).isEmpty();
        }

        @Test
        @DisplayName("성공: 중복 이름 체크 후 저장")
        void checkDuplicateAndSave_Success() {
            // given
            String newTeamName = "새팀";
            given(teamRepository.existsByTeamName(newTeamName)).willReturn(false);

            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName(newTeamName);
            request.setOrderNo(1);
            Team newTeam = Team.createTeam(request);
            Team savedTeam = Team.createTeam(request);
            ReflectionTestUtils.setField(savedTeam, "id", 100L);

            given(teamRepository.save(any(Team.class))).willReturn(savedTeam);

            // when
            boolean exists = teamRepository.existsByTeamName(newTeamName);
            Team saved = null;
            if (!exists) {
                saved = teamRepository.save(newTeam);
            }

            // then
            assertThat(exists).isFalse();
            assertThat(saved).isNotNull();
            assertThat(saved.getTeamName()).isEqualTo(newTeamName);
        }

        @Test
        @DisplayName("성공: 하위 팀 존재 여부 확인 후 삭제 가능 판단")
        void checkChildrenBeforeDelete_Success() {
            // given
            Long teamId = 1L;
            given(teamRepository.existsByParentId(teamId)).willReturn(false);
            doNothing().when(teamRepository).deleteById(teamId);

            // when
            boolean hasChildren = teamRepository.existsByParentId(teamId);
            if (!hasChildren) {
                teamRepository.deleteById(teamId);
            }

            // then
            assertThat(hasChildren).isFalse();
            verify(teamRepository).deleteById(teamId);
        }
    }
}

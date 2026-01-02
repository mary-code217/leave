package com.hoho.leave.domain.org.entity;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.domain.org.dto.request.TeamCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Team 엔티티 테스트")
class TeamTest {

    private Team team;
    private TeamCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new TeamCreateRequest();
        createRequest.setTeamName("개발본부");
        createRequest.setOrderNo(1);
        createRequest.setParentId(null);

        team = Team.createTeam(createRequest);
        ReflectionTestUtils.setField(team, "id", 1L);
    }

    @Nested
    @DisplayName("팀 생성")
    class CreateTeam {

        @Test
        @DisplayName("성공: createTeam 정적 메서드로 루트 팀을 생성한다")
        void createTeam_RootTeam_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("경영지원본부");
            request.setOrderNo(2);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam).isNotNull();
            assertThat(createdTeam.getTeamName()).isEqualTo("경영지원본부");
            assertThat(createdTeam.getOrderNo()).isEqualTo(2);
            assertThat(createdTeam.getParent()).isNull();
        }

        @Test
        @DisplayName("성공: orderNo가 null인 팀을 생성한다")
        void createTeam_NullOrderNo_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("임시팀");
            request.setOrderNo(null);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam).isNotNull();
            assertThat(createdTeam.getTeamName()).isEqualTo("임시팀");
            assertThat(createdTeam.getOrderNo()).isNull();
        }

        @Test
        @DisplayName("성공: orderNo가 0인 팀을 생성한다")
        void createTeam_ZeroOrderNo_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("신규팀");
            request.setOrderNo(0);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam).isNotNull();
            assertThat(createdTeam.getTeamName()).isEqualTo("신규팀");
            assertThat(createdTeam.getOrderNo()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("하위 팀 생성")
    class CreateChild {

        @Test
        @DisplayName("성공: 하위 팀을 생성한다")
        void createChild_Success() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("백엔드팀");
            childRequest.setOrderNo(1);

            // when
            Team child = team.createChild(childRequest);

            // then
            assertThat(child).isNotNull();
            assertThat(child.getTeamName()).isEqualTo("백엔드팀");
            assertThat(child.getParent()).isEqualTo(team);
            assertThat(team.getChildren()).contains(child);
        }

        @Test
        @DisplayName("성공: 여러 하위 팀을 생성한다")
        void createChild_Multiple_Success() {
            // given
            TeamCreateRequest childRequest1 = new TeamCreateRequest();
            childRequest1.setTeamName("백엔드팀");
            childRequest1.setOrderNo(1);

            TeamCreateRequest childRequest2 = new TeamCreateRequest();
            childRequest2.setTeamName("프론트엔드팀");
            childRequest2.setOrderNo(2);

            // when
            Team child1 = team.createChild(childRequest1);
            Team child2 = team.createChild(childRequest2);

            // then
            assertThat(team.getChildren()).hasSize(2);
            assertThat(team.getChildren()).contains(child1, child2);
            assertThat(child1.getParent()).isEqualTo(team);
            assertThat(child2.getParent()).isEqualTo(team);
        }

        @Test
        @DisplayName("성공: 손자 팀을 생성한다")
        void createChild_GrandChild_Success() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("백엔드팀");
            childRequest.setOrderNo(1);

            TeamCreateRequest grandChildRequest = new TeamCreateRequest();
            grandChildRequest.setTeamName("API개발파트");
            grandChildRequest.setOrderNo(1);

            // when
            Team child = team.createChild(childRequest);
            Team grandChild = child.createChild(grandChildRequest);

            // then
            assertThat(grandChild.getParent()).isEqualTo(child);
            assertThat(child.getChildren()).contains(grandChild);
            assertThat(grandChild.getTeamName()).isEqualTo("API개발파트");
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: teamName 필드가 올바르게 저장된다")
        void teamName_StoredCorrectly() {
            // given
            String expectedTeamName = "개발본부";

            // when
            String actualTeamName = team.getTeamName();

            // then
            assertThat(actualTeamName).isEqualTo(expectedTeamName);
        }

        @Test
        @DisplayName("성공: orderNo 필드가 올바르게 저장된다")
        void orderNo_StoredCorrectly() {
            // given
            Integer expectedOrderNo = 1;

            // when
            Integer actualOrderNo = team.getOrderNo();

            // then
            assertThat(actualOrderNo).isEqualTo(expectedOrderNo);
        }

        @Test
        @DisplayName("성공: 루트 팀의 parent는 null이다")
        void parent_RootTeam_IsNull() {
            // when
            Team parent = team.getParent();

            // then
            assertThat(parent).isNull();
        }

        @Test
        @DisplayName("성공: 초기 children은 빈 리스트이다")
        void children_InitiallyEmpty() {
            // when & then
            assertThat(team.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("테스트팀");
            request.setOrderNo(1);
            Team newTeam = Team.createTeam(request);
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(newTeam, "id", expectedId);
            Long actualId = newTeam.getId();

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("팀명 변경")
    class Rename {

        @Test
        @DisplayName("성공: 팀명을 변경한다")
        void rename_Success() {
            // given
            String newTeamName = "기술본부";

            // when
            team.rename(newTeamName);

            // then
            assertThat(team.getTeamName()).isEqualTo("기술본부");
        }

        @Test
        @DisplayName("성공: 한글 팀명으로 변경한다")
        void rename_KoreanName_Success() {
            // given
            String newTeamName = "연구개발센터";

            // when
            team.rename(newTeamName);

            // then
            assertThat(team.getTeamName()).isEqualTo("연구개발센터");
        }

        @Test
        @DisplayName("성공: 영문 팀명으로 변경한다")
        void rename_EnglishName_Success() {
            // given
            String newTeamName = "R&D Center";

            // when
            team.rename(newTeamName);

            // then
            assertThat(team.getTeamName()).isEqualTo("R&D Center");
        }

        @Test
        @DisplayName("실패: null로 변경 시 예외가 발생한다")
        void rename_NullName_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> team.rename(null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("팀명은 비어 있을 수 없습니다");
        }

        @Test
        @DisplayName("실패: 빈 문자열로 변경 시 예외가 발생한다")
        void rename_EmptyName_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> team.rename(""))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("팀명은 비어 있을 수 없습니다");
        }

        @Test
        @DisplayName("실패: 공백 문자열로 변경 시 예외가 발생한다")
        void rename_BlankName_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> team.rename("   "))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("팀명은 비어 있을 수 없습니다");
        }
    }

    @Nested
    @DisplayName("정렬 순서 변경")
    class ChangeOrder {

        @Test
        @DisplayName("성공: 정렬 순서를 변경한다")
        void changeOrder_Success() {
            // given
            Integer newOrderNo = 5;

            // when
            team.changeOrder(newOrderNo);

            // then
            assertThat(team.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 정렬 순서를 0으로 변경한다")
        void changeOrder_ToZero_Success() {
            // given
            Integer newOrderNo = 0;

            // when
            team.changeOrder(newOrderNo);

            // then
            assertThat(team.getOrderNo()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: null을 전달하면 정렬 순서가 변경되지 않는다")
        void changeOrder_NullValue_NoChange() {
            // given
            Integer originalOrderNo = team.getOrderNo();

            // when
            team.changeOrder(null);

            // then
            assertThat(team.getOrderNo()).isEqualTo(originalOrderNo);
        }
    }

    @Nested
    @DisplayName("상위 팀 이동")
    class MoveTo {

        @Test
        @DisplayName("성공: 상위 팀을 변경한다")
        void moveTo_Success() {
            // given
            TeamCreateRequest parentRequest = new TeamCreateRequest();
            parentRequest.setTeamName("신규본부");
            parentRequest.setOrderNo(1);
            Team newParent = Team.createTeam(parentRequest);
            ReflectionTestUtils.setField(newParent, "id", 10L);

            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("하위팀");
            childRequest.setOrderNo(1);
            Team child = team.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 2L);

            // when
            child.moveTo(newParent);

            // then
            assertThat(child.getParent()).isEqualTo(newParent);
            assertThat(newParent.getChildren()).contains(child);
            assertThat(team.getChildren()).doesNotContain(child);
        }

        @Test
        @DisplayName("성공: 루트 팀으로 변경한다")
        void moveTo_ToRoot_Success() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("하위팀");
            childRequest.setOrderNo(1);
            Team child = team.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 2L);

            // when
            child.moveTo(null);

            // then
            assertThat(child.getParent()).isNull();
            assertThat(team.getChildren()).doesNotContain(child);
        }

        @Test
        @DisplayName("성공: 동일한 부모로 이동 시 변경 없음")
        void moveTo_SameParent_NoChange() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("하위팀");
            childRequest.setOrderNo(1);
            Team child = team.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 2L);
            int originalChildrenCount = team.getChildren().size();

            // when
            child.moveTo(team);

            // then
            assertThat(child.getParent()).isEqualTo(team);
            assertThat(team.getChildren()).hasSize(originalChildrenCount);
        }

        @Test
        @DisplayName("실패: 자기 자신을 상위 팀으로 지정할 수 없다")
        void moveTo_Self_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> team.moveTo(team))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("자기 자신을 상위 부서로 지정할 수 없습니다");
        }

        @Test
        @DisplayName("실패: 하위 팀으로 이동할 수 없다")
        void moveTo_ToChild_ThrowsException() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("하위팀");
            childRequest.setOrderNo(1);
            Team child = team.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 2L);

            // when & then
            assertThatThrownBy(() -> team.moveTo(child))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("하위 부서로 이동할 수 없습니다");
        }

        @Test
        @DisplayName("실패: 손자 팀으로 이동할 수 없다")
        void moveTo_ToGrandChild_ThrowsException() {
            // given
            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("하위팀");
            childRequest.setOrderNo(1);
            Team child = team.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 2L);

            TeamCreateRequest grandChildRequest = new TeamCreateRequest();
            grandChildRequest.setTeamName("손자팀");
            grandChildRequest.setOrderNo(1);
            Team grandChild = child.createChild(grandChildRequest);
            ReflectionTestUtils.setField(grandChild, "id", 3L);

            // when & then
            assertThatThrownBy(() -> team.moveTo(grandChild))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("하위 부서로 이동할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("다양한 팀명 형식")
    class VariousTeamNameFormats {

        @Test
        @DisplayName("성공: 짧은 팀명을 저장한다")
        void shortTeamName_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("본부");
            request.setOrderNo(1);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam.getTeamName()).isEqualTo("본부");
        }

        @Test
        @DisplayName("성공: 긴 팀명을 저장한다")
        void longTeamName_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("글로벌 비즈니스 전략 및 개발 본부");
            request.setOrderNo(1);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam.getTeamName()).isEqualTo("글로벌 비즈니스 전략 및 개발 본부");
        }

        @Test
        @DisplayName("성공: 특수문자가 포함된 팀명을 저장한다")
        void specialCharactersTeamName_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("R&D팀(연구개발)");
            request.setOrderNo(1);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam.getTeamName()).isEqualTo("R&D팀(연구개발)");
        }

        @Test
        @DisplayName("성공: 숫자가 포함된 팀명을 저장한다")
        void numberInTeamName_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("개발1팀");
            request.setOrderNo(1);

            // when
            Team createdTeam = Team.createTeam(request);

            // then
            assertThat(createdTeam.getTeamName()).isEqualTo("개발1팀");
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 팀 생성 후 이름과 순서를 모두 변경한다")
        void createAndModify_Success() {
            // given
            TeamCreateRequest request = new TeamCreateRequest();
            request.setTeamName("임시팀");
            request.setOrderNo(10);

            Team newTeam = Team.createTeam(request);

            // when
            newTeam.rename("정식팀");
            newTeam.changeOrder(5);

            // then
            assertThat(newTeam.getTeamName()).isEqualTo("정식팀");
            assertThat(newTeam.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 복잡한 계층 구조를 생성한다")
        void createComplexHierarchy_Success() {
            // given
            TeamCreateRequest level1Request = new TeamCreateRequest();
            level1Request.setTeamName("Level1");
            level1Request.setOrderNo(1);

            TeamCreateRequest level2aRequest = new TeamCreateRequest();
            level2aRequest.setTeamName("Level2-A");
            level2aRequest.setOrderNo(1);

            TeamCreateRequest level2bRequest = new TeamCreateRequest();
            level2bRequest.setTeamName("Level2-B");
            level2bRequest.setOrderNo(2);

            TeamCreateRequest level3Request = new TeamCreateRequest();
            level3Request.setTeamName("Level3");
            level3Request.setOrderNo(1);

            // when
            Team level1 = Team.createTeam(level1Request);
            ReflectionTestUtils.setField(level1, "id", 1L);

            Team level2a = level1.createChild(level2aRequest);
            ReflectionTestUtils.setField(level2a, "id", 2L);

            Team level2b = level1.createChild(level2bRequest);
            ReflectionTestUtils.setField(level2b, "id", 3L);

            Team level3 = level2a.createChild(level3Request);
            ReflectionTestUtils.setField(level3, "id", 4L);

            // then
            assertThat(level1.getChildren()).hasSize(2);
            assertThat(level2a.getParent()).isEqualTo(level1);
            assertThat(level2b.getParent()).isEqualTo(level1);
            assertThat(level3.getParent()).isEqualTo(level2a);
            assertThat(level2a.getChildren()).contains(level3);
        }

        @Test
        @DisplayName("성공: 팀 이동 후 계층 구조가 올바르게 변경된다")
        void moveTeamAndVerifyHierarchy_Success() {
            // given
            TeamCreateRequest parentARequest = new TeamCreateRequest();
            parentARequest.setTeamName("본부A");
            parentARequest.setOrderNo(1);
            Team parentA = Team.createTeam(parentARequest);
            ReflectionTestUtils.setField(parentA, "id", 1L);

            TeamCreateRequest parentBRequest = new TeamCreateRequest();
            parentBRequest.setTeamName("본부B");
            parentBRequest.setOrderNo(2);
            Team parentB = Team.createTeam(parentBRequest);
            ReflectionTestUtils.setField(parentB, "id", 2L);

            TeamCreateRequest childRequest = new TeamCreateRequest();
            childRequest.setTeamName("이동할팀");
            childRequest.setOrderNo(1);
            Team child = parentA.createChild(childRequest);
            ReflectionTestUtils.setField(child, "id", 3L);

            // when
            child.moveTo(parentB);

            // then
            assertThat(parentA.getChildren()).isEmpty();
            assertThat(parentB.getChildren()).contains(child);
            assertThat(child.getParent()).isEqualTo(parentB);
        }
    }
}

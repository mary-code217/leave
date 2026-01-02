package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Position 엔티티 테스트")
class PositionTest {

    private Position position;
    private PositionCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new PositionCreateRequest();
        createRequest.setPositionName("팀장");
        createRequest.setOrderNo(1);

        position = Position.create(createRequest);
    }

    @Nested
    @DisplayName("직책 생성")
    class CreatePosition {

        @Test
        @DisplayName("성공: create 정적 메서드로 직책을 생성한다")
        void create_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("파트장");
            request.setOrderNo(2);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition).isNotNull();
            assertThat(createdPosition.getPositionName()).isEqualTo("파트장");
            assertThat(createdPosition.getOrderNo()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: orderNo가 null인 직책을 생성한다")
        void create_NullOrderNo_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("담당");
            request.setOrderNo(null);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition).isNotNull();
            assertThat(createdPosition.getPositionName()).isEqualTo("담당");
            assertThat(createdPosition.getOrderNo()).isNull();
        }

        @Test
        @DisplayName("성공: orderNo가 0인 직책을 생성한다")
        void create_ZeroOrderNo_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("인턴");
            request.setOrderNo(0);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition).isNotNull();
            assertThat(createdPosition.getPositionName()).isEqualTo("인턴");
            assertThat(createdPosition.getOrderNo()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: positionName 필드가 올바르게 저장된다")
        void positionName_StoredCorrectly() {
            // given
            String expectedPositionName = "팀장";

            // when
            String actualPositionName = position.getPositionName();

            // then
            assertThat(actualPositionName).isEqualTo(expectedPositionName);
        }

        @Test
        @DisplayName("성공: orderNo 필드가 올바르게 저장된다")
        void orderNo_StoredCorrectly() {
            // given
            Integer expectedOrderNo = 1;

            // when
            Integer actualOrderNo = position.getOrderNo();

            // then
            assertThat(actualOrderNo).isEqualTo(expectedOrderNo);
        }

        @Test
        @DisplayName("성공: id 필드는 초기값이 null이다")
        void id_InitiallyNull() {
            // when
            Long id = position.getId();

            // then
            assertThat(id).isNull();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(position, "id", expectedId);
            Long actualId = position.getId();

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("직책명 변경")
    class Rename {

        @Test
        @DisplayName("성공: 직책명을 변경한다")
        void rename_Success() {
            // given
            String newPositionName = "실장";

            // when
            position.rename(newPositionName);

            // then
            assertThat(position.getPositionName()).isEqualTo("실장");
        }

        @Test
        @DisplayName("성공: 한글 직책명으로 변경한다")
        void rename_KoreanName_Success() {
            // given
            String newPositionName = "수석연구원";

            // when
            position.rename(newPositionName);

            // then
            assertThat(position.getPositionName()).isEqualTo("수석연구원");
        }

        @Test
        @DisplayName("성공: 영문 직책명으로 변경한다")
        void rename_EnglishName_Success() {
            // given
            String newPositionName = "Team Leader";

            // when
            position.rename(newPositionName);

            // then
            assertThat(position.getPositionName()).isEqualTo("Team Leader");
        }
    }

    @Nested
    @DisplayName("정렬 순서 변경")
    class ChangeOrderNo {

        @Test
        @DisplayName("성공: 정렬 순서를 변경한다")
        void changeOrderNo_Success() {
            // given
            Integer newOrderNo = 5;

            // when
            position.changeOrderNo(newOrderNo);

            // then
            assertThat(position.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 정렬 순서를 0으로 변경한다")
        void changeOrderNo_ToZero_Success() {
            // given
            Integer newOrderNo = 0;

            // when
            position.changeOrderNo(newOrderNo);

            // then
            assertThat(position.getOrderNo()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: null을 전달하면 정렬 순서가 변경되지 않는다")
        void changeOrderNo_NullValue_NoChange() {
            // given
            Integer originalOrderNo = position.getOrderNo();

            // when
            position.changeOrderNo(null);

            // then
            assertThat(position.getOrderNo()).isEqualTo(originalOrderNo);
        }

        @Test
        @DisplayName("성공: 큰 정렬 순서 값으로 변경한다")
        void changeOrderNo_LargeValue_Success() {
            // given
            Integer newOrderNo = 999;

            // when
            position.changeOrderNo(newOrderNo);

            // then
            assertThat(position.getOrderNo()).isEqualTo(999);
        }
    }

    @Nested
    @DisplayName("다양한 직책명 형식")
    class VariousPositionNameFormats {

        @Test
        @DisplayName("성공: 짧은 직책명을 저장한다")
        void shortPositionName_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("대표");
            request.setOrderNo(1);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition.getPositionName()).isEqualTo("대표");
        }

        @Test
        @DisplayName("성공: 긴 직책명을 저장한다")
        void longPositionName_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("프로젝트 총괄 책임자");
            request.setOrderNo(1);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition.getPositionName()).isEqualTo("프로젝트 총괄 책임자");
        }

        @Test
        @DisplayName("성공: 특수문자가 포함된 직책명을 저장한다")
        void specialCharactersPositionName_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("팀장(겸임)");
            request.setOrderNo(1);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition.getPositionName()).isEqualTo("팀장(겸임)");
        }

        @Test
        @DisplayName("성공: 숫자가 포함된 직책명을 저장한다")
        void numberInPositionName_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("1팀 팀장");
            request.setOrderNo(1);

            // when
            Position createdPosition = Position.create(request);

            // then
            assertThat(createdPosition.getPositionName()).isEqualTo("1팀 팀장");
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 직책 생성 후 이름과 순서를 모두 변경한다")
        void createAndModify_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("담당자");
            request.setOrderNo(10);

            Position newPosition = Position.create(request);

            // when
            newPosition.rename("책임자");
            newPosition.changeOrderNo(5);

            // then
            assertThat(newPosition.getPositionName()).isEqualTo("책임자");
            assertThat(newPosition.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 여러 번 이름을 변경한다")
        void multipleRename_Success() {
            // given & when
            position.rename("파트장");
            assertThat(position.getPositionName()).isEqualTo("파트장");

            position.rename("실장");
            assertThat(position.getPositionName()).isEqualTo("실장");

            position.rename("본부장");
            assertThat(position.getPositionName()).isEqualTo("본부장");

            // then
            assertThat(position.getPositionName()).isEqualTo("본부장");
        }

        @Test
        @DisplayName("성공: 여러 번 정렬 순서를 변경한다")
        void multipleChangeOrderNo_Success() {
            // given & when
            position.changeOrderNo(1);
            assertThat(position.getOrderNo()).isEqualTo(1);

            position.changeOrderNo(2);
            assertThat(position.getOrderNo()).isEqualTo(2);

            position.changeOrderNo(3);
            assertThat(position.getOrderNo()).isEqualTo(3);

            // then
            assertThat(position.getOrderNo()).isEqualTo(3);
        }
    }
}

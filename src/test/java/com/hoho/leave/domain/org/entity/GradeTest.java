package com.hoho.leave.domain.org.entity;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Grade 엔티티 테스트")
class GradeTest {

    private Grade grade;
    private GradeCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        createRequest = new GradeCreateRequest();
        createRequest.setGradeName("과장");
        createRequest.setOrderNo(1);

        grade = Grade.create(createRequest);
    }

    @Nested
    @DisplayName("직급 생성")
    class CreateGrade {

        @Test
        @DisplayName("성공: create 정적 메서드로 직급을 생성한다")
        void create_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("대리");
            request.setOrderNo(2);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade).isNotNull();
            assertThat(createdGrade.getGradeName()).isEqualTo("대리");
            assertThat(createdGrade.getOrderNo()).isEqualTo(2);
        }

        @Test
        @DisplayName("성공: orderNo가 null인 직급을 생성한다")
        void create_NullOrderNo_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("사원");
            request.setOrderNo(null);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade).isNotNull();
            assertThat(createdGrade.getGradeName()).isEqualTo("사원");
            assertThat(createdGrade.getOrderNo()).isNull();
        }

        @Test
        @DisplayName("성공: orderNo가 0인 직급을 생성한다")
        void create_ZeroOrderNo_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("인턴");
            request.setOrderNo(0);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade).isNotNull();
            assertThat(createdGrade.getGradeName()).isEqualTo("인턴");
            assertThat(createdGrade.getOrderNo()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: gradeName 필드가 올바르게 저장된다")
        void gradeName_StoredCorrectly() {
            // given
            String expectedGradeName = "과장";

            // when
            String actualGradeName = grade.getGradeName();

            // then
            assertThat(actualGradeName).isEqualTo(expectedGradeName);
        }

        @Test
        @DisplayName("성공: orderNo 필드가 올바르게 저장된다")
        void orderNo_StoredCorrectly() {
            // given
            Integer expectedOrderNo = 1;

            // when
            Integer actualOrderNo = grade.getOrderNo();

            // then
            assertThat(actualOrderNo).isEqualTo(expectedOrderNo);
        }

        @Test
        @DisplayName("성공: id 필드는 초기값이 null이다")
        void id_InitiallyNull() {
            // when
            Long id = grade.getId();

            // then
            assertThat(id).isNull();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(grade, "id", expectedId);
            Long actualId = grade.getId();

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("직급명 변경")
    class Rename {

        @Test
        @DisplayName("성공: 직급명을 변경한다")
        void rename_Success() {
            // given
            String newGradeName = "차장";

            // when
            grade.rename(newGradeName);

            // then
            assertThat(grade.getGradeName()).isEqualTo("차장");
        }

        @Test
        @DisplayName("성공: 한글 직급명으로 변경한다")
        void rename_KoreanName_Success() {
            // given
            String newGradeName = "수석연구원";

            // when
            grade.rename(newGradeName);

            // then
            assertThat(grade.getGradeName()).isEqualTo("수석연구원");
        }

        @Test
        @DisplayName("성공: 영문 직급명으로 변경한다")
        void rename_EnglishName_Success() {
            // given
            String newGradeName = "Senior Manager";

            // when
            grade.rename(newGradeName);

            // then
            assertThat(grade.getGradeName()).isEqualTo("Senior Manager");
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
            grade.changeOrderNo(newOrderNo);

            // then
            assertThat(grade.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 정렬 순서를 0으로 변경한다")
        void changeOrderNo_ToZero_Success() {
            // given
            Integer newOrderNo = 0;

            // when
            grade.changeOrderNo(newOrderNo);

            // then
            assertThat(grade.getOrderNo()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: null을 전달하면 정렬 순서가 변경되지 않는다")
        void changeOrderNo_NullValue_NoChange() {
            // given
            Integer originalOrderNo = grade.getOrderNo();

            // when
            grade.changeOrderNo(null);

            // then
            assertThat(grade.getOrderNo()).isEqualTo(originalOrderNo);
        }

        @Test
        @DisplayName("성공: 큰 정렬 순서 값으로 변경한다")
        void changeOrderNo_LargeValue_Success() {
            // given
            Integer newOrderNo = 999;

            // when
            grade.changeOrderNo(newOrderNo);

            // then
            assertThat(grade.getOrderNo()).isEqualTo(999);
        }
    }

    @Nested
    @DisplayName("다양한 직급명 형식")
    class VariousGradeNameFormats {

        @Test
        @DisplayName("성공: 짧은 직급명을 저장한다")
        void shortGradeName_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("팀장");
            request.setOrderNo(1);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade.getGradeName()).isEqualTo("팀장");
        }

        @Test
        @DisplayName("성공: 긴 직급명을 저장한다")
        void longGradeName_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("프로젝트 매니저 겸 기술 리더");
            request.setOrderNo(1);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade.getGradeName()).isEqualTo("프로젝트 매니저 겸 기술 리더");
        }

        @Test
        @DisplayName("성공: 특수문자가 포함된 직급명을 저장한다")
        void specialCharactersGradeName_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("과장(1급)");
            request.setOrderNo(1);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade.getGradeName()).isEqualTo("과장(1급)");
        }

        @Test
        @DisplayName("성공: 숫자가 포함된 직급명을 저장한다")
        void numberInGradeName_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("1급 정규직");
            request.setOrderNo(1);

            // when
            Grade createdGrade = Grade.create(request);

            // then
            assertThat(createdGrade.getGradeName()).isEqualTo("1급 정규직");
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 직급 생성 후 이름과 순서를 모두 변경한다")
        void createAndModify_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("사원");
            request.setOrderNo(10);

            Grade newGrade = Grade.create(request);

            // when
            newGrade.rename("주임");
            newGrade.changeOrderNo(5);

            // then
            assertThat(newGrade.getGradeName()).isEqualTo("주임");
            assertThat(newGrade.getOrderNo()).isEqualTo(5);
        }

        @Test
        @DisplayName("성공: 여러 번 이름을 변경한다")
        void multipleRename_Success() {
            // given & when
            grade.rename("대리");
            assertThat(grade.getGradeName()).isEqualTo("대리");

            grade.rename("과장");
            assertThat(grade.getGradeName()).isEqualTo("과장");

            grade.rename("차장");
            assertThat(grade.getGradeName()).isEqualTo("차장");

            // then
            assertThat(grade.getGradeName()).isEqualTo("차장");
        }

        @Test
        @DisplayName("성공: 여러 번 정렬 순서를 변경한다")
        void multipleChangeOrderNo_Success() {
            // given & when
            grade.changeOrderNo(1);
            assertThat(grade.getOrderNo()).isEqualTo(1);

            grade.changeOrderNo(2);
            assertThat(grade.getOrderNo()).isEqualTo(2);

            grade.changeOrderNo(3);
            assertThat(grade.getOrderNo()).isEqualTo(3);

            // then
            assertThat(grade.getOrderNo()).isEqualTo(3);
        }
    }
}

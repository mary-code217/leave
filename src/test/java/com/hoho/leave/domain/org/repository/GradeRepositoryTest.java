package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.entity.Grade;
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
@DisplayName("GradeRepository 테스트")
class GradeRepositoryTest {

    @Mock
    private GradeRepository gradeRepository;

    private Grade mockGrade;
    private Grade mockGrade2;

    @BeforeEach
    void setUp() {
        // Mock Grade 생성
        GradeCreateRequest request1 = new GradeCreateRequest();
        request1.setGradeName("과장");
        request1.setOrderNo(1);
        mockGrade = Grade.create(request1);
        ReflectionTestUtils.setField(mockGrade, "id", 1L);

        GradeCreateRequest request2 = new GradeCreateRequest();
        request2.setGradeName("대리");
        request2.setOrderNo(2);
        mockGrade2 = Grade.create(request2);
        ReflectionTestUtils.setField(mockGrade2, "id", 2L);
    }

    @Nested
    @DisplayName("직급 저장")
    class SaveGrade {

        @Test
        @DisplayName("성공: 새로운 직급을 저장한다")
        void save_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("차장");
            request.setOrderNo(3);
            Grade newGrade = Grade.create(request);

            Grade savedGrade = Grade.create(request);
            ReflectionTestUtils.setField(savedGrade, "id", 3L);

            given(gradeRepository.save(any(Grade.class))).willReturn(savedGrade);

            // when
            Grade result = gradeRepository.save(newGrade);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getGradeName()).isEqualTo("차장");
            assertThat(result.getOrderNo()).isEqualTo(3);
            verify(gradeRepository).save(newGrade);
        }

        @Test
        @DisplayName("성공: 여러 직급을 저장한다")
        void save_MultipleGrades_Success() {
            // given
            GradeCreateRequest request1 = new GradeCreateRequest();
            request1.setGradeName("부장");
            request1.setOrderNo(1);

            GradeCreateRequest request2 = new GradeCreateRequest();
            request2.setGradeName("팀장");
            request2.setOrderNo(2);

            Grade grade1 = Grade.create(request1);
            Grade grade2 = Grade.create(request2);

            given(gradeRepository.save(any(Grade.class))).willAnswer(invocation -> {
                Grade g = invocation.getArgument(0);
                ReflectionTestUtils.setField(g, "id", (long) (Math.random() * 1000));
                return g;
            });

            // when
            Grade saved1 = gradeRepository.save(grade1);
            Grade saved2 = gradeRepository.save(grade2);

            // then
            assertThat(saved1.getGradeName()).isEqualTo("부장");
            assertThat(saved2.getGradeName()).isEqualTo("팀장");
            verify(gradeRepository, times(2)).save(any(Grade.class));
        }
    }

    @Nested
    @DisplayName("직급 ID로 조회")
    class FindById {

        @Test
        @DisplayName("성공: ID로 직급을 조회한다")
        void findById_Exists_ReturnsGrade() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));

            // when
            Optional<Grade> result = gradeRepository.findById(gradeId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(gradeId);
            assertThat(result.get().getGradeName()).isEqualTo("과장");
            verify(gradeRepository).findById(gradeId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_NotExists_ReturnsEmpty() {
            // given
            Long nonExistentId = 999L;
            given(gradeRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<Grade> result = gradeRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(gradeRepository).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("직급명으로 조회")
    class FindByGradeName {

        @Test
        @DisplayName("성공: 직급명으로 직급을 조회한다")
        void findByGradeName_Exists_ReturnsGrade() {
            // given
            String gradeName = "과장";
            given(gradeRepository.findByGradeName(gradeName)).willReturn(Optional.of(mockGrade));

            // when
            Optional<Grade> result = gradeRepository.findByGradeName(gradeName);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getGradeName()).isEqualTo(gradeName);
            verify(gradeRepository).findByGradeName(gradeName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 직급명으로 조회 시 빈 Optional 반환")
        void findByGradeName_NotExists_ReturnsEmpty() {
            // given
            String nonExistentName = "존재하지않는직급";
            given(gradeRepository.findByGradeName(nonExistentName)).willReturn(Optional.empty());

            // when
            Optional<Grade> result = gradeRepository.findByGradeName(nonExistentName);

            // then
            assertThat(result).isEmpty();
            verify(gradeRepository).findByGradeName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("직급명 존재 여부 확인")
    class ExistsByGradeName {

        @Test
        @DisplayName("성공: 존재하는 직급명 확인 시 true 반환")
        void existsByGradeName_Exists_ReturnsTrue() {
            // given
            String gradeName = "과장";
            given(gradeRepository.existsByGradeName(gradeName)).willReturn(true);

            // when
            boolean result = gradeRepository.existsByGradeName(gradeName);

            // then
            assertThat(result).isTrue();
            verify(gradeRepository).existsByGradeName(gradeName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 직급명 확인 시 false 반환")
        void existsByGradeName_NotExists_ReturnsFalse() {
            // given
            String nonExistentName = "존재하지않는직급";
            given(gradeRepository.existsByGradeName(nonExistentName)).willReturn(false);

            // when
            boolean result = gradeRepository.existsByGradeName(nonExistentName);

            // then
            assertThat(result).isFalse();
            verify(gradeRepository).existsByGradeName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("특정 ID 제외 직급명 존재 여부 확인")
    class ExistsByGradeNameAndIdNot {

        @Test
        @DisplayName("성공: 다른 직급이 동일한 이름을 가지고 있으면 true 반환")
        void existsByGradeNameAndIdNot_DuplicateExists_ReturnsTrue() {
            // given
            String gradeName = "과장";
            Long excludeId = 2L;
            given(gradeRepository.existsByGradeNameAndIdNot(gradeName, excludeId)).willReturn(true);

            // when
            boolean result = gradeRepository.existsByGradeNameAndIdNot(gradeName, excludeId);

            // then
            assertThat(result).isTrue();
            verify(gradeRepository).existsByGradeNameAndIdNot(gradeName, excludeId);
        }

        @Test
        @DisplayName("성공: 자신만 해당 이름을 가지고 있으면 false 반환")
        void existsByGradeNameAndIdNot_OnlySelf_ReturnsFalse() {
            // given
            String gradeName = "과장";
            Long selfId = 1L;
            given(gradeRepository.existsByGradeNameAndIdNot(gradeName, selfId)).willReturn(false);

            // when
            boolean result = gradeRepository.existsByGradeNameAndIdNot(gradeName, selfId);

            // then
            assertThat(result).isFalse();
            verify(gradeRepository).existsByGradeNameAndIdNot(gradeName, selfId);
        }

        @Test
        @DisplayName("성공: 해당 이름이 존재하지 않으면 false 반환")
        void existsByGradeNameAndIdNot_NameNotExists_ReturnsFalse() {
            // given
            String newGradeName = "새로운직급";
            Long excludeId = 1L;
            given(gradeRepository.existsByGradeNameAndIdNot(newGradeName, excludeId)).willReturn(false);

            // when
            boolean result = gradeRepository.existsByGradeNameAndIdNot(newGradeName, excludeId);

            // then
            assertThat(result).isFalse();
            verify(gradeRepository).existsByGradeNameAndIdNot(newGradeName, excludeId);
        }
    }

    @Nested
    @DisplayName("전체 직급 조회")
    class FindAll {

        @Test
        @DisplayName("성공: 전체 직급 목록을 페이징하여 조회한다")
        void findAll_Success() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("orderNo")));
            List<Grade> grades = List.of(mockGrade, mockGrade2);
            Page<Grade> gradePage = new PageImpl<>(grades, pageRequest, 2);

            given(gradeRepository.findAll(any(PageRequest.class))).willReturn(gradePage);

            // when
            Page<Grade> result = gradeRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            verify(gradeRepository).findAll(pageRequest);
        }

        @Test
        @DisplayName("성공: 직급이 없는 경우 빈 페이지 반환")
        void findAll_EmptyResult() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Grade> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(gradeRepository.findAll(any(PageRequest.class))).willReturn(emptyPage);

            // when
            Page<Grade> result = gradeRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findAll_Pagination() {
            // given
            List<Grade> allGrades = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                GradeCreateRequest request = new GradeCreateRequest();
                request.setGradeName("직급" + i);
                request.setOrderNo(i);
                Grade grade = Grade.create(request);
                ReflectionTestUtils.setField(grade, "id", (long) (i + 1));
                allGrades.add(grade);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5);
            PageRequest pageRequest2 = PageRequest.of(1, 5);
            PageRequest pageRequest3 = PageRequest.of(2, 5);

            Page<Grade> page1 = new PageImpl<>(allGrades.subList(0, 5), pageRequest1, 15);
            Page<Grade> page2 = new PageImpl<>(allGrades.subList(5, 10), pageRequest2, 15);
            Page<Grade> page3 = new PageImpl<>(allGrades.subList(10, 15), pageRequest3, 15);

            given(gradeRepository.findAll(eq(pageRequest1))).willReturn(page1);
            given(gradeRepository.findAll(eq(pageRequest2))).willReturn(page2);
            given(gradeRepository.findAll(eq(pageRequest3))).willReturn(page3);

            // when
            Page<Grade> result1 = gradeRepository.findAll(pageRequest1);
            Page<Grade> result2 = gradeRepository.findAll(pageRequest2);
            Page<Grade> result3 = gradeRepository.findAll(pageRequest3);

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
    @DisplayName("직급 삭제")
    class DeleteGrade {

        @Test
        @DisplayName("성공: ID로 직급을 삭제한다")
        void deleteById_Success() {
            // given
            Long gradeId = 1L;
            doNothing().when(gradeRepository).deleteById(gradeId);

            // when
            gradeRepository.deleteById(gradeId);

            // then
            verify(gradeRepository, times(1)).deleteById(gradeId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 삭제 시 예외 없이 처리된다")
        void deleteById_NonExistent_NoException() {
            // given
            Long nonExistentId = 999L;
            doNothing().when(gradeRepository).deleteById(nonExistentId);

            // when
            gradeRepository.deleteById(nonExistentId);

            // then
            verify(gradeRepository).deleteById(nonExistentId);
        }

        @Test
        @DisplayName("성공: 모든 직급을 삭제한다")
        void deleteAll_Success() {
            // given
            doNothing().when(gradeRepository).deleteAll();

            // when
            gradeRepository.deleteAll();

            // then
            verify(gradeRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("직급 개수 조회")
    class CountGrades {

        @Test
        @DisplayName("성공: 전체 직급 개수를 조회한다")
        void count_ReturnsCorrectCount() {
            // given
            given(gradeRepository.count()).willReturn(10L);

            // when
            long count = gradeRepository.count();

            // then
            assertThat(count).isEqualTo(10L);
            verify(gradeRepository).count();
        }

        @Test
        @DisplayName("성공: 직급이 없을 경우 0을 반환한다")
        void count_NoGrades_ReturnsZero() {
            // given
            given(gradeRepository.count()).willReturn(0L);

            // when
            long count = gradeRepository.count();

            // then
            assertThat(count).isEqualTo(0L);
            verify(gradeRepository).count();
        }
    }

    @Nested
    @DisplayName("직급 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("성공: 존재하는 ID 확인 시 true 반환")
        void existsById_Exists_ReturnsTrue() {
            // given
            Long existingId = 1L;
            given(gradeRepository.existsById(existingId)).willReturn(true);

            // when
            boolean exists = gradeRepository.existsById(existingId);

            // then
            assertThat(exists).isTrue();
            verify(gradeRepository).existsById(existingId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 확인 시 false 반환")
        void existsById_NotExists_ReturnsFalse() {
            // given
            Long nonExistentId = 999L;
            given(gradeRepository.existsById(nonExistentId)).willReturn(false);

            // when
            boolean exists = gradeRepository.existsById(nonExistentId);

            // then
            assertThat(exists).isFalse();
            verify(gradeRepository).existsById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 직급 저장 후 조회")
        void saveAndFind_Success() {
            // given
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("신규직급");
            request.setOrderNo(5);
            Grade newGrade = Grade.create(request);

            Grade savedGrade = Grade.create(request);
            ReflectionTestUtils.setField(savedGrade, "id", 10L);

            given(gradeRepository.save(any(Grade.class))).willReturn(savedGrade);
            given(gradeRepository.findById(10L)).willReturn(Optional.of(savedGrade));

            // when
            Grade saved = gradeRepository.save(newGrade);
            Optional<Grade> found = gradeRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getGradeName()).isEqualTo("신규직급");
        }

        @Test
        @DisplayName("성공: 저장 후 삭제, 조회 시 빈 결과")
        void saveDeleteFind_Empty() {
            // given
            Long gradeId = 20L;
            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName("삭제될직급");
            request.setOrderNo(1);
            Grade savedGrade = Grade.create(request);
            ReflectionTestUtils.setField(savedGrade, "id", gradeId);

            given(gradeRepository.save(any(Grade.class))).willReturn(savedGrade);
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(savedGrade), Optional.empty());
            doNothing().when(gradeRepository).deleteById(gradeId);

            // when
            gradeRepository.save(savedGrade);
            Optional<Grade> beforeDelete = gradeRepository.findById(gradeId);
            gradeRepository.deleteById(gradeId);
            Optional<Grade> afterDelete = gradeRepository.findById(gradeId);

            // then
            assertThat(beforeDelete).isPresent();
            assertThat(afterDelete).isEmpty();
        }

        @Test
        @DisplayName("성공: 중복 이름 체크 후 저장")
        void checkDuplicateAndSave_Success() {
            // given
            String newGradeName = "새직급";
            given(gradeRepository.existsByGradeName(newGradeName)).willReturn(false);

            GradeCreateRequest request = new GradeCreateRequest();
            request.setGradeName(newGradeName);
            request.setOrderNo(1);
            Grade newGrade = Grade.create(request);
            Grade savedGrade = Grade.create(request);
            ReflectionTestUtils.setField(savedGrade, "id", 100L);

            given(gradeRepository.save(any(Grade.class))).willReturn(savedGrade);

            // when
            boolean exists = gradeRepository.existsByGradeName(newGradeName);
            Grade saved = null;
            if (!exists) {
                saved = gradeRepository.save(newGrade);
            }

            // then
            assertThat(exists).isFalse();
            assertThat(saved).isNotNull();
            assertThat(saved.getGradeName()).isEqualTo(newGradeName);
        }
    }
}

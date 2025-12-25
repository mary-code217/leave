package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.GradeCreateRequest;
import com.hoho.leave.domain.org.dto.request.GradeUpdateRequest;
import com.hoho.leave.domain.org.dto.response.GradeDetailResponse;
import com.hoho.leave.domain.org.dto.response.GradeListResponse;
import com.hoho.leave.domain.org.entity.Grade;
import com.hoho.leave.domain.org.repository.GradeRepository;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GradeService 테스트")
class GradeServiceTest {

    @InjectMocks
    private GradeService gradeService;

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private UserRepository userRepository;

    private GradeCreateRequest createRequest;
    private GradeUpdateRequest updateRequest;
    private Grade mockGrade;

    @BeforeEach
    void setUp() {
        // 목 데이터 설정
        createRequest = new GradeCreateRequest();
        createRequest.setGradeName("사원");
        createRequest.setOrderNo(1);

        updateRequest = new GradeUpdateRequest();
        updateRequest.setGradeName("대리");
        updateRequest.setOrderNo(2);

        mockGrade = Grade.create(createRequest);
    }

    @Nested
    @DisplayName("직급 생성")
    class CreateGrade {

        @Test
        @DisplayName("성공: 정상적으로 직급을 생성한다")
        void createGrade_Success() {
            // given
            given(gradeRepository.existsByGradeName("사원")).willReturn(false);
            given(gradeRepository.save(any(Grade.class))).willReturn(mockGrade);

            // when
            gradeService.createGrade(createRequest);

            // then
            verify(gradeRepository).existsByGradeName("사원");
            verify(gradeRepository).save(any(Grade.class));
        }

        @Test
        @DisplayName("실패: 중복된 직급명으로 생성 시 예외 발생")
        void createGrade_DuplicateName_ThrowsException() {
            // given
            given(gradeRepository.existsByGradeName("사원")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> gradeService.createGrade(createRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate Grade");

            verify(gradeRepository).existsByGradeName("사원");
            verify(gradeRepository, never()).save(any(Grade.class));
        }
    }

    @Nested
    @DisplayName("직급 수정")
    class UpdateGrade {

        @Test
        @DisplayName("성공: 정상적으로 직급을 수정한다")
        void updateGrade_Success() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));
            given(gradeRepository.existsByGradeNameAndIdNot("대리", gradeId)).willReturn(false);

            // when
            gradeService.updateGrade(gradeId, updateRequest);

            // then
            verify(gradeRepository).findById(gradeId);
            verify(gradeRepository).existsByGradeNameAndIdNot("대리", gradeId);
            assertThat(mockGrade.getGradeName()).isEqualTo("대리");
            assertThat(mockGrade.getOrderNo()).isEqualTo(2);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직급 수정 시 예외 발생")
        void updateGrade_NotFound_ThrowsException() {
            // given
            Long gradeId = 999L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> gradeService.updateGrade(gradeId, updateRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Grade");
        }

        @Test
        @DisplayName("실패: 중복된 직급명으로 수정 시 예외 발생")
        void updateGrade_DuplicateName_ThrowsException() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));
            given(gradeRepository.existsByGradeNameAndIdNot("대리", gradeId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> gradeService.updateGrade(gradeId, updateRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate Grade");
        }
    }

    @Nested
    @DisplayName("직급 삭제")
    class DeleteGrade {

        @Test
        @DisplayName("성공: 정상적으로 직급을 삭제한다")
        void deleteGrade_Success() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));
            given(userRepository.existsByGradeId(gradeId)).willReturn(false);

            // when
            gradeService.deleteGrade(gradeId);

            // then
            verify(gradeRepository).findById(gradeId);
            verify(userRepository).existsByGradeId(gradeId);
            verify(gradeRepository).delete(mockGrade);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직급 삭제 시 예외 발생")
        void deleteGrade_NotFound_ThrowsException() {
            // given
            Long gradeId = 999L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> gradeService.deleteGrade(gradeId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Grade");
        }

        @Test
        @DisplayName("실패: 소속 사용자가 있는 직급 삭제 시 예외 발생")
        void deleteGrade_ExistUser_ThrowsException() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));
            given(userRepository.existsByGradeId(gradeId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> gradeService.deleteGrade(gradeId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("소속 사용자가 존재합니다");

            verify(gradeRepository, never()).delete(any(Grade.class));
        }
    }

    @Nested
    @DisplayName("직급 조회")
    class GetGrade {

        @Test
        @DisplayName("성공: 특정 직급을 조회한다")
        void getGrade_Success() {
            // given
            Long gradeId = 1L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.of(mockGrade));

            // when
            GradeDetailResponse response = gradeService.getGrade(gradeId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getGradeName()).isEqualTo("사원");
            assertThat(response.getOrderNo()).isEqualTo(1);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직급 조회 시 예외 발생")
        void getGrade_NotFound_ThrowsException() {
            // given
            Long gradeId = 999L;
            given(gradeRepository.findById(gradeId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> gradeService.getGrade(gradeId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found Grade");
        }
    }

    @Nested
    @DisplayName("직급 목록 조회")
    class GetAllGrades {

        @Test
        @DisplayName("성공: 전체 직급 목록을 페이지네이션으로 조회한다")
        void getAllGrades_Success() {
            // given
            GradeCreateRequest request2 = new GradeCreateRequest();
            request2.setGradeName("대리");
            request2.setOrderNo(2);
            Grade grade2 = Grade.create(request2);

            List<Grade> grades = List.of(mockGrade, grade2);
            Page<Grade> gradePage = new PageImpl<>(grades);

            given(gradeRepository.findAll(any(Pageable.class))).willReturn(gradePage);

            // when
            GradeListResponse response = gradeService.getAllGrades(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getGrades()).hasSize(2);
            verify(gradeRepository).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 빈 목록 조회 시 빈 응답 반환")
        void getAllGrades_EmptyList() {
            // given
            Page<Grade> emptyPage = new PageImpl<>(List.of());
            given(gradeRepository.findAll(any(Pageable.class))).willReturn(emptyPage);

            // when
            GradeListResponse response = gradeService.getAllGrades(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getGrades()).isEmpty();
        }
    }
}

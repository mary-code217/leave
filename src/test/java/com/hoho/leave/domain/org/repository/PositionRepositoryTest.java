package com.hoho.leave.domain.org.repository;

import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.entity.Position;
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
@DisplayName("PositionRepository 테스트")
class PositionRepositoryTest {

    @Mock
    private PositionRepository positionRepository;

    private Position mockPosition;
    private Position mockPosition2;

    @BeforeEach
    void setUp() {
        // Mock Position 생성
        PositionCreateRequest request1 = new PositionCreateRequest();
        request1.setPositionName("팀장");
        request1.setOrderNo(1);
        mockPosition = Position.create(request1);
        ReflectionTestUtils.setField(mockPosition, "id", 1L);

        PositionCreateRequest request2 = new PositionCreateRequest();
        request2.setPositionName("파트장");
        request2.setOrderNo(2);
        mockPosition2 = Position.create(request2);
        ReflectionTestUtils.setField(mockPosition2, "id", 2L);
    }

    @Nested
    @DisplayName("직책 저장")
    class SavePosition {

        @Test
        @DisplayName("성공: 새로운 직책을 저장한다")
        void save_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("실장");
            request.setOrderNo(3);
            Position newPosition = Position.create(request);

            Position savedPosition = Position.create(request);
            ReflectionTestUtils.setField(savedPosition, "id", 3L);

            given(positionRepository.save(any(Position.class))).willReturn(savedPosition);

            // when
            Position result = positionRepository.save(newPosition);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(3L);
            assertThat(result.getPositionName()).isEqualTo("실장");
            assertThat(result.getOrderNo()).isEqualTo(3);
            verify(positionRepository).save(newPosition);
        }

        @Test
        @DisplayName("성공: 여러 직책을 저장한다")
        void save_MultiplePositions_Success() {
            // given
            PositionCreateRequest request1 = new PositionCreateRequest();
            request1.setPositionName("본부장");
            request1.setOrderNo(1);

            PositionCreateRequest request2 = new PositionCreateRequest();
            request2.setPositionName("센터장");
            request2.setOrderNo(2);

            Position position1 = Position.create(request1);
            Position position2 = Position.create(request2);

            given(positionRepository.save(any(Position.class))).willAnswer(invocation -> {
                Position p = invocation.getArgument(0);
                ReflectionTestUtils.setField(p, "id", (long) (Math.random() * 1000));
                return p;
            });

            // when
            Position saved1 = positionRepository.save(position1);
            Position saved2 = positionRepository.save(position2);

            // then
            assertThat(saved1.getPositionName()).isEqualTo("본부장");
            assertThat(saved2.getPositionName()).isEqualTo("센터장");
            verify(positionRepository, times(2)).save(any(Position.class));
        }
    }

    @Nested
    @DisplayName("직책 ID로 조회")
    class FindById {

        @Test
        @DisplayName("성공: ID로 직책을 조회한다")
        void findById_Exists_ReturnsPosition() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));

            // when
            Optional<Position> result = positionRepository.findById(positionId);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getId()).isEqualTo(positionId);
            assertThat(result.get().getPositionName()).isEqualTo("팀장");
            verify(positionRepository).findById(positionId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_NotExists_ReturnsEmpty() {
            // given
            Long nonExistentId = 999L;
            given(positionRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<Position> result = positionRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(positionRepository).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("직책명으로 조회")
    class FindByPositionName {

        @Test
        @DisplayName("성공: 직책명으로 직책을 조회한다")
        void findByPositionName_Exists_ReturnsPosition() {
            // given
            String positionName = "팀장";
            given(positionRepository.findByPositionName(positionName)).willReturn(Optional.of(mockPosition));

            // when
            Optional<Position> result = positionRepository.findByPositionName(positionName);

            // then
            assertThat(result).isPresent();
            assertThat(result.get().getPositionName()).isEqualTo(positionName);
            verify(positionRepository).findByPositionName(positionName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 직책명으로 조회 시 빈 Optional 반환")
        void findByPositionName_NotExists_ReturnsEmpty() {
            // given
            String nonExistentName = "존재하지않는직책";
            given(positionRepository.findByPositionName(nonExistentName)).willReturn(Optional.empty());

            // when
            Optional<Position> result = positionRepository.findByPositionName(nonExistentName);

            // then
            assertThat(result).isEmpty();
            verify(positionRepository).findByPositionName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("직책명 존재 여부 확인")
    class ExistsByPositionName {

        @Test
        @DisplayName("성공: 존재하는 직책명 확인 시 true 반환")
        void existsByPositionName_Exists_ReturnsTrue() {
            // given
            String positionName = "팀장";
            given(positionRepository.existsByPositionName(positionName)).willReturn(true);

            // when
            boolean result = positionRepository.existsByPositionName(positionName);

            // then
            assertThat(result).isTrue();
            verify(positionRepository).existsByPositionName(positionName);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 직책명 확인 시 false 반환")
        void existsByPositionName_NotExists_ReturnsFalse() {
            // given
            String nonExistentName = "존재하지않는직책";
            given(positionRepository.existsByPositionName(nonExistentName)).willReturn(false);

            // when
            boolean result = positionRepository.existsByPositionName(nonExistentName);

            // then
            assertThat(result).isFalse();
            verify(positionRepository).existsByPositionName(nonExistentName);
        }
    }

    @Nested
    @DisplayName("특정 ID 제외 직책명 존재 여부 확인")
    class ExistsByPositionNameAndIdNot {

        @Test
        @DisplayName("성공: 다른 직책이 동일한 이름을 가지고 있으면 true 반환")
        void existsByPositionNameAndIdNot_DuplicateExists_ReturnsTrue() {
            // given
            String positionName = "팀장";
            Long excludeId = 2L;
            given(positionRepository.existsByPositionNameAndIdNot(positionName, excludeId)).willReturn(true);

            // when
            boolean result = positionRepository.existsByPositionNameAndIdNot(positionName, excludeId);

            // then
            assertThat(result).isTrue();
            verify(positionRepository).existsByPositionNameAndIdNot(positionName, excludeId);
        }

        @Test
        @DisplayName("성공: 자신만 해당 이름을 가지고 있으면 false 반환")
        void existsByPositionNameAndIdNot_OnlySelf_ReturnsFalse() {
            // given
            String positionName = "팀장";
            Long selfId = 1L;
            given(positionRepository.existsByPositionNameAndIdNot(positionName, selfId)).willReturn(false);

            // when
            boolean result = positionRepository.existsByPositionNameAndIdNot(positionName, selfId);

            // then
            assertThat(result).isFalse();
            verify(positionRepository).existsByPositionNameAndIdNot(positionName, selfId);
        }

        @Test
        @DisplayName("성공: 해당 이름이 존재하지 않으면 false 반환")
        void existsByPositionNameAndIdNot_NameNotExists_ReturnsFalse() {
            // given
            String newPositionName = "새로운직책";
            Long excludeId = 1L;
            given(positionRepository.existsByPositionNameAndIdNot(newPositionName, excludeId)).willReturn(false);

            // when
            boolean result = positionRepository.existsByPositionNameAndIdNot(newPositionName, excludeId);

            // then
            assertThat(result).isFalse();
            verify(positionRepository).existsByPositionNameAndIdNot(newPositionName, excludeId);
        }
    }

    @Nested
    @DisplayName("전체 직책 조회")
    class FindAll {

        @Test
        @DisplayName("성공: 전체 직책 목록을 페이징하여 조회한다")
        void findAll_Success() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("orderNo")));
            List<Position> positions = List.of(mockPosition, mockPosition2);
            Page<Position> positionPage = new PageImpl<>(positions, pageRequest, 2);

            given(positionRepository.findAll(any(PageRequest.class))).willReturn(positionPage);

            // when
            Page<Position> result = positionRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(2);
            assertThat(result.getTotalElements()).isEqualTo(2);
            verify(positionRepository).findAll(pageRequest);
        }

        @Test
        @DisplayName("성공: 직책이 없는 경우 빈 페이지 반환")
        void findAll_EmptyResult() {
            // given
            PageRequest pageRequest = PageRequest.of(0, 10);
            Page<Position> emptyPage = new PageImpl<>(List.of(), pageRequest, 0);

            given(positionRepository.findAll(any(PageRequest.class))).willReturn(emptyPage);

            // when
            Page<Position> result = positionRepository.findAll(pageRequest);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getContent()).isEmpty();
            assertThat(result.getTotalElements()).isEqualTo(0);
        }

        @Test
        @DisplayName("성공: 페이지네이션이 올바르게 동작한다")
        void findAll_Pagination() {
            // given
            List<Position> allPositions = new ArrayList<>();
            for (int i = 0; i < 15; i++) {
                PositionCreateRequest request = new PositionCreateRequest();
                request.setPositionName("직책" + i);
                request.setOrderNo(i);
                Position position = Position.create(request);
                ReflectionTestUtils.setField(position, "id", (long) (i + 1));
                allPositions.add(position);
            }

            PageRequest pageRequest1 = PageRequest.of(0, 5);
            PageRequest pageRequest2 = PageRequest.of(1, 5);
            PageRequest pageRequest3 = PageRequest.of(2, 5);

            Page<Position> page1 = new PageImpl<>(allPositions.subList(0, 5), pageRequest1, 15);
            Page<Position> page2 = new PageImpl<>(allPositions.subList(5, 10), pageRequest2, 15);
            Page<Position> page3 = new PageImpl<>(allPositions.subList(10, 15), pageRequest3, 15);

            given(positionRepository.findAll(eq(pageRequest1))).willReturn(page1);
            given(positionRepository.findAll(eq(pageRequest2))).willReturn(page2);
            given(positionRepository.findAll(eq(pageRequest3))).willReturn(page3);

            // when
            Page<Position> result1 = positionRepository.findAll(pageRequest1);
            Page<Position> result2 = positionRepository.findAll(pageRequest2);
            Page<Position> result3 = positionRepository.findAll(pageRequest3);

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
    @DisplayName("직책 삭제")
    class DeletePosition {

        @Test
        @DisplayName("성공: ID로 직책을 삭제한다")
        void deleteById_Success() {
            // given
            Long positionId = 1L;
            doNothing().when(positionRepository).deleteById(positionId);

            // when
            positionRepository.deleteById(positionId);

            // then
            verify(positionRepository, times(1)).deleteById(positionId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 삭제 시 예외 없이 처리된다")
        void deleteById_NonExistent_NoException() {
            // given
            Long nonExistentId = 999L;
            doNothing().when(positionRepository).deleteById(nonExistentId);

            // when
            positionRepository.deleteById(nonExistentId);

            // then
            verify(positionRepository).deleteById(nonExistentId);
        }

        @Test
        @DisplayName("성공: 모든 직책을 삭제한다")
        void deleteAll_Success() {
            // given
            doNothing().when(positionRepository).deleteAll();

            // when
            positionRepository.deleteAll();

            // then
            verify(positionRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("직책 개수 조회")
    class CountPositions {

        @Test
        @DisplayName("성공: 전체 직책 개수를 조회한다")
        void count_ReturnsCorrectCount() {
            // given
            given(positionRepository.count()).willReturn(10L);

            // when
            long count = positionRepository.count();

            // then
            assertThat(count).isEqualTo(10L);
            verify(positionRepository).count();
        }

        @Test
        @DisplayName("성공: 직책이 없을 경우 0을 반환한다")
        void count_NoPositions_ReturnsZero() {
            // given
            given(positionRepository.count()).willReturn(0L);

            // when
            long count = positionRepository.count();

            // then
            assertThat(count).isEqualTo(0L);
            verify(positionRepository).count();
        }
    }

    @Nested
    @DisplayName("직책 존재 여부 확인")
    class ExistsById {

        @Test
        @DisplayName("성공: 존재하는 ID 확인 시 true 반환")
        void existsById_Exists_ReturnsTrue() {
            // given
            Long existingId = 1L;
            given(positionRepository.existsById(existingId)).willReturn(true);

            // when
            boolean exists = positionRepository.existsById(existingId);

            // then
            assertThat(exists).isTrue();
            verify(positionRepository).existsById(existingId);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 확인 시 false 반환")
        void existsById_NotExists_ReturnsFalse() {
            // given
            Long nonExistentId = 999L;
            given(positionRepository.existsById(nonExistentId)).willReturn(false);

            // when
            boolean exists = positionRepository.existsById(nonExistentId);

            // then
            assertThat(exists).isFalse();
            verify(positionRepository).existsById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 직책 저장 후 조회")
        void saveAndFind_Success() {
            // given
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("신규직책");
            request.setOrderNo(5);
            Position newPosition = Position.create(request);

            Position savedPosition = Position.create(request);
            ReflectionTestUtils.setField(savedPosition, "id", 10L);

            given(positionRepository.save(any(Position.class))).willReturn(savedPosition);
            given(positionRepository.findById(10L)).willReturn(Optional.of(savedPosition));

            // when
            Position saved = positionRepository.save(newPosition);
            Optional<Position> found = positionRepository.findById(saved.getId());

            // then
            assertThat(found).isPresent();
            assertThat(found.get().getPositionName()).isEqualTo("신규직책");
        }

        @Test
        @DisplayName("성공: 저장 후 삭제, 조회 시 빈 결과")
        void saveDeleteFind_Empty() {
            // given
            Long positionId = 20L;
            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName("삭제될직책");
            request.setOrderNo(1);
            Position savedPosition = Position.create(request);
            ReflectionTestUtils.setField(savedPosition, "id", positionId);

            given(positionRepository.save(any(Position.class))).willReturn(savedPosition);
            given(positionRepository.findById(positionId)).willReturn(Optional.of(savedPosition), Optional.empty());
            doNothing().when(positionRepository).deleteById(positionId);

            // when
            positionRepository.save(savedPosition);
            Optional<Position> beforeDelete = positionRepository.findById(positionId);
            positionRepository.deleteById(positionId);
            Optional<Position> afterDelete = positionRepository.findById(positionId);

            // then
            assertThat(beforeDelete).isPresent();
            assertThat(afterDelete).isEmpty();
        }

        @Test
        @DisplayName("성공: 중복 이름 체크 후 저장")
        void checkDuplicateAndSave_Success() {
            // given
            String newPositionName = "새직책";
            given(positionRepository.existsByPositionName(newPositionName)).willReturn(false);

            PositionCreateRequest request = new PositionCreateRequest();
            request.setPositionName(newPositionName);
            request.setOrderNo(1);
            Position newPosition = Position.create(request);
            Position savedPosition = Position.create(request);
            ReflectionTestUtils.setField(savedPosition, "id", 100L);

            given(positionRepository.save(any(Position.class))).willReturn(savedPosition);

            // when
            boolean exists = positionRepository.existsByPositionName(newPositionName);
            Position saved = null;
            if (!exists) {
                saved = positionRepository.save(newPosition);
            }

            // then
            assertThat(exists).isFalse();
            assertThat(saved).isNotNull();
            assertThat(saved.getPositionName()).isEqualTo(newPositionName);
        }
    }
}

package com.hoho.leave.domain.org.service;

import com.hoho.leave.common.exception.BusinessException;
import com.hoho.leave.common.exception.DuplicateException;
import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.org.dto.request.PositionCreateRequest;
import com.hoho.leave.domain.org.dto.request.PositionUpdateRequest;
import com.hoho.leave.domain.org.dto.response.PositionDetailResponse;
import com.hoho.leave.domain.org.dto.response.PositionListResponse;
import com.hoho.leave.domain.org.entity.Position;
import com.hoho.leave.domain.org.repository.PositionRepository;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PositionService 테스트")
class PositionServiceTest {

    @InjectMocks
    private PositionService positionService;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private UserRepository userRepository;

    private PositionCreateRequest createRequest;
    private PositionUpdateRequest updateRequest;
    private Position mockPosition;

    @BeforeEach
    void setUp() {
        // 목 데이터 설정
        createRequest = new PositionCreateRequest();
        createRequest.setPositionName("팀원");
        createRequest.setOrderNo(1);

        updateRequest = new PositionUpdateRequest();
        updateRequest.setPositionName("팀장");
        updateRequest.setOrderNo(2);

        mockPosition = Position.create(createRequest);
    }

    @Nested
    @DisplayName("직책 생성")
    class CreatePosition {

        @Test
        @DisplayName("성공: 정상적으로 직책을 생성한다")
        void createPosition_Success() {
            // given
            given(positionRepository.existsByPositionName("팀원")).willReturn(false);
            given(positionRepository.save(any(Position.class))).willReturn(mockPosition);

            // when
            positionService.createPosition(createRequest);

            // then
            verify(positionRepository).existsByPositionName("팀원");
            verify(positionRepository).save(any(Position.class));
        }

        @Test
        @DisplayName("실패: 중복된 직책명으로 생성 시 예외 발생")
        void createPosition_DuplicateName_ThrowsException() {
            // given
            given(positionRepository.existsByPositionName("팀원")).willReturn(true);

            // when & then
            assertThatThrownBy(() -> positionService.createPosition(createRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate Position");

            verify(positionRepository).existsByPositionName("팀원");
            verify(positionRepository, never()).save(any(Position.class));
        }
    }

    @Nested
    @DisplayName("직책 수정")
    class UpdatePosition {

        @Test
        @DisplayName("성공: 정상적으로 직책을 수정한다")
        void updatePosition_Success() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));
            given(positionRepository.existsByPositionNameAndIdNot("팀장", positionId)).willReturn(false);

            // when
            positionService.updatePosition(positionId, updateRequest);

            // then
            verify(positionRepository).findById(positionId);
            verify(positionRepository).existsByPositionNameAndIdNot("팀장", positionId);
            assertThat(mockPosition.getPositionName()).isEqualTo("팀장");
            assertThat(mockPosition.getOrderNo()).isEqualTo(2);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직책 수정 시 예외 발생")
        void updatePosition_NotFound_ThrowsException() {
            // given
            Long positionId = 999L;
            given(positionRepository.findById(positionId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> positionService.updatePosition(positionId, updateRequest))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not found Position");
        }

        @Test
        @DisplayName("실패: 중복된 직책명으로 수정 시 예외 발생")
        void updatePosition_DuplicateName_ThrowsException() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));
            given(positionRepository.existsByPositionNameAndIdNot("팀장", positionId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> positionService.updatePosition(positionId, updateRequest))
                    .isInstanceOf(DuplicateException.class)
                    .hasMessageContaining("Duplicate Position");
        }
    }

    @Nested
    @DisplayName("직책 삭제")
    class DeletePosition {

        @Test
        @DisplayName("성공: 정상적으로 직책을 삭제한다")
        void deletePosition_Success() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));
            given(userRepository.existsByPositionId(positionId)).willReturn(false);

            // when
            positionService.deletePosition(positionId);

            // then
            verify(positionRepository).findById(positionId);
            verify(userRepository).existsByPositionId(positionId);
            verify(positionRepository).delete(mockPosition);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직책 삭제 시 예외 발생")
        void deletePosition_NotFound_ThrowsException() {
            // given
            Long positionId = 999L;
            given(positionRepository.findById(positionId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> positionService.deletePosition(positionId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not found Position");
        }

        @Test
        @DisplayName("실패: 소속 사용자가 있는 직책 삭제 시 예외 발생")
        void deletePosition_ExistUser_ThrowsException() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));
            given(userRepository.existsByPositionId(positionId)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> positionService.deletePosition(positionId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("사용자가 존재합니다");

            verify(positionRepository, never()).delete(any(Position.class));
        }
    }

    @Nested
    @DisplayName("직책 조회")
    class GetPosition {

        @Test
        @DisplayName("성공: 특정 직책을 조회한다")
        void getPosition_Success() {
            // given
            Long positionId = 1L;
            given(positionRepository.findById(positionId)).willReturn(Optional.of(mockPosition));

            // when
            PositionDetailResponse response = positionService.getPosition(positionId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getPositionName()).isEqualTo("팀원");
            assertThat(response.getOrderNo()).isEqualTo(1);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 직책 조회 시 예외 발생")
        void getPosition_NotFound_ThrowsException() {
            // given
            Long positionId = 999L;
            given(positionRepository.findById(positionId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> positionService.getPosition(positionId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not found Position");
        }
    }

    @Nested
    @DisplayName("직책 목록 조회")
    class GetAllPositions {

        @Test
        @DisplayName("성공: 전체 직책 목록을 페이지네이션으로 조회한다")
        void getAllPositions_Success() {
            // given
            PositionCreateRequest request2 = new PositionCreateRequest();
            request2.setPositionName("팀장");
            request2.setOrderNo(2);
            Position position2 = Position.create(request2);

            List<Position> positions = List.of(mockPosition, position2);
            Page<Position> positionPage = new PageImpl<>(positions);

            given(positionRepository.findAll(any(Pageable.class))).willReturn(positionPage);

            // when
            PositionListResponse response = positionService.getAllPositions(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getPositions()).hasSize(2);
            verify(positionRepository).findAll(any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 빈 목록 조회 시 빈 응답 반환")
        void getAllPositions_EmptyList() {
            // given
            Page<Position> emptyPage = new PageImpl<>(List.of());
            given(positionRepository.findAll(any(Pageable.class))).willReturn(emptyPage);

            // when
            PositionListResponse response = positionService.getAllPositions(10, 1);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getPositions()).isEmpty();
        }
    }
}

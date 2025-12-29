package com.hoho.leave.domain.auth.repository;

import com.hoho.leave.domain.auth.RefreshEntity;
import com.hoho.leave.domain.auth.RefreshRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RefreshRepository 테스트")
class RefreshRepositoryTest {

    @Mock
    private RefreshRepository refreshRepository;

    private RefreshEntity mockRefreshEntity;
    private String userEmail;
    private String refreshToken;
    private String expiration;

    @BeforeEach
    void setUp() {
        userEmail = "test@example.com";
        refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh.token";
        expiration = "2025-12-30T10:00:00";

        mockRefreshEntity = new RefreshEntity(userEmail, refreshToken, expiration);
        ReflectionTestUtils.setField(mockRefreshEntity, "id", 1L);
    }

    @Nested
    @DisplayName("리프레시 토큰 존재 여부 확인")
    class ExistsByRefresh {

        @Test
        @DisplayName("성공: 존재하는 리프레시 토큰 조회 시 true 반환")
        void existsByRefresh_TokenExists_ReturnsTrue() {
            // given
            given(refreshRepository.existsByRefresh(refreshToken)).willReturn(true);

            // when
            Boolean result = refreshRepository.existsByRefresh(refreshToken);

            // then
            assertThat(result).isTrue();
            verify(refreshRepository).existsByRefresh(refreshToken);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 리프레시 토큰 조회 시 false 반환")
        void existsByRefresh_TokenNotExists_ReturnsFalse() {
            // given
            String nonExistentToken = "non-existent-token";
            given(refreshRepository.existsByRefresh(nonExistentToken)).willReturn(false);

            // when
            Boolean result = refreshRepository.existsByRefresh(nonExistentToken);

            // then
            assertThat(result).isFalse();
            verify(refreshRepository).existsByRefresh(nonExistentToken);
        }

        @Test
        @DisplayName("성공: null 토큰 조회 시 false 반환")
        void existsByRefresh_NullToken_ReturnsFalse() {
            // given
            given(refreshRepository.existsByRefresh(null)).willReturn(false);

            // when
            Boolean result = refreshRepository.existsByRefresh(null);

            // then
            assertThat(result).isFalse();
            verify(refreshRepository).existsByRefresh(null);
        }

        @Test
        @DisplayName("성공: 빈 문자열 토큰 조회 시 false 반환")
        void existsByRefresh_EmptyToken_ReturnsFalse() {
            // given
            String emptyToken = "";
            given(refreshRepository.existsByRefresh(emptyToken)).willReturn(false);

            // when
            Boolean result = refreshRepository.existsByRefresh(emptyToken);

            // then
            assertThat(result).isFalse();
            verify(refreshRepository).existsByRefresh(emptyToken);
        }

        @Test
        @DisplayName("성공: 만료된 토큰도 존재하면 true 반환")
        void existsByRefresh_ExpiredTokenExists_ReturnsTrue() {
            // given
            String expiredToken = "expired-refresh-token";
            given(refreshRepository.existsByRefresh(expiredToken)).willReturn(true);

            // when
            Boolean result = refreshRepository.existsByRefresh(expiredToken);

            // then
            assertThat(result).isTrue();
            verify(refreshRepository).existsByRefresh(expiredToken);
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 삭제")
    class DeleteByRefresh {

        @Test
        @DisplayName("성공: 존재하는 리프레시 토큰을 삭제한다")
        void deleteByRefresh_TokenExists_DeletesSuccessfully() {
            // given
            doNothing().when(refreshRepository).deleteByRefresh(refreshToken);

            // when
            refreshRepository.deleteByRefresh(refreshToken);

            // then
            verify(refreshRepository, times(1)).deleteByRefresh(refreshToken);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 토큰 삭제 시 예외 없이 처리된다")
        void deleteByRefresh_TokenNotExists_NoException() {
            // given
            String nonExistentToken = "non-existent-token";
            doNothing().when(refreshRepository).deleteByRefresh(nonExistentToken);

            // when
            refreshRepository.deleteByRefresh(nonExistentToken);

            // then
            verify(refreshRepository).deleteByRefresh(nonExistentToken);
        }

        @Test
        @DisplayName("성공: null 토큰 삭제 시 예외 없이 처리된다")
        void deleteByRefresh_NullToken_NoException() {
            // given
            doNothing().when(refreshRepository).deleteByRefresh(null);

            // when
            refreshRepository.deleteByRefresh(null);

            // then
            verify(refreshRepository).deleteByRefresh(null);
        }

        @Test
        @DisplayName("성공: 삭제 후 해당 토큰이 존재하지 않는다")
        void deleteByRefresh_AfterDelete_TokenNotExists() {
            // given
            given(refreshRepository.existsByRefresh(refreshToken)).willReturn(true, false);
            doNothing().when(refreshRepository).deleteByRefresh(refreshToken);

            // when
            Boolean beforeDelete = refreshRepository.existsByRefresh(refreshToken);
            refreshRepository.deleteByRefresh(refreshToken);
            Boolean afterDelete = refreshRepository.existsByRefresh(refreshToken);

            // then
            assertThat(beforeDelete).isTrue();
            assertThat(afterDelete).isFalse();
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 저장")
    class SaveRefreshToken {

        @Test
        @DisplayName("성공: 새로운 리프레시 토큰을 저장한다")
        void save_NewToken_Success() {
            // given
            RefreshEntity newEntity = new RefreshEntity("new@example.com", "new-token", "2025-12-31T23:59:59");
            RefreshEntity savedEntity = new RefreshEntity("new@example.com", "new-token", "2025-12-31T23:59:59");
            ReflectionTestUtils.setField(savedEntity, "id", 2L);

            given(refreshRepository.save(any(RefreshEntity.class))).willReturn(savedEntity);

            // when
            RefreshEntity result = refreshRepository.save(newEntity);

            // then
            assertThat(result).isNotNull();
            Long id = (Long) ReflectionTestUtils.getField(result, "id");
            assertThat(id).isEqualTo(2L);
            verify(refreshRepository).save(newEntity);
        }

        @Test
        @DisplayName("성공: 동일한 사용자의 새 토큰을 저장한다")
        void save_SameUserNewToken_Success() {
            // given
            RefreshEntity oldEntity = new RefreshEntity(userEmail, "old-token", "2025-12-29T10:00:00");
            ReflectionTestUtils.setField(oldEntity, "id", 1L);

            RefreshEntity newEntity = new RefreshEntity(userEmail, "new-token", "2025-12-30T10:00:00");
            RefreshEntity savedEntity = new RefreshEntity(userEmail, "new-token", "2025-12-30T10:00:00");
            ReflectionTestUtils.setField(savedEntity, "id", 2L);

            given(refreshRepository.save(newEntity)).willReturn(savedEntity);

            // when
            RefreshEntity result = refreshRepository.save(newEntity);

            // then
            assertThat(result).isNotNull();
            String storedEmail = ReflectionTestUtils.getField(result, "userEmail").toString();
            String storedToken = ReflectionTestUtils.getField(result, "refresh").toString();
            assertThat(storedEmail).isEqualTo(userEmail);
            assertThat(storedToken).isEqualTo("new-token");
        }

        @Test
        @DisplayName("성공: 여러 사용자의 토큰을 개별적으로 저장한다")
        void save_MultipleUsers_Success() {
            // given
            RefreshEntity entity1 = new RefreshEntity("user1@example.com", "token1", "2025-12-31T23:59:59");
            RefreshEntity entity2 = new RefreshEntity("user2@example.com", "token2", "2025-12-31T23:59:59");
            RefreshEntity entity3 = new RefreshEntity("user3@example.com", "token3", "2025-12-31T23:59:59");

            given(refreshRepository.save(any(RefreshEntity.class))).willAnswer(invocation -> {
                RefreshEntity e = invocation.getArgument(0);
                ReflectionTestUtils.setField(e, "id", (long) (Math.random() * 1000));
                return e;
            });

            // when
            RefreshEntity saved1 = refreshRepository.save(entity1);
            RefreshEntity saved2 = refreshRepository.save(entity2);
            RefreshEntity saved3 = refreshRepository.save(entity3);

            // then
            assertThat(saved1).isNotNull();
            assertThat(saved2).isNotNull();
            assertThat(saved3).isNotNull();
            verify(refreshRepository, times(3)).save(any(RefreshEntity.class));
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 ID로 조회")
    class FindById {

        @Test
        @DisplayName("성공: ID로 리프레시 토큰을 조회한다")
        void findById_Exists_ReturnsEntity() {
            // given
            Long id = 1L;
            given(refreshRepository.findById(id)).willReturn(Optional.of(mockRefreshEntity));

            // when
            Optional<RefreshEntity> result = refreshRepository.findById(id);

            // then
            assertThat(result).isPresent();
            String storedEmail = ReflectionTestUtils.getField(result.get(), "userEmail").toString();
            assertThat(storedEmail).isEqualTo(userEmail);
            verify(refreshRepository).findById(id);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID로 조회 시 빈 Optional 반환")
        void findById_NotExists_ReturnsEmpty() {
            // given
            Long nonExistentId = 999L;
            given(refreshRepository.findById(nonExistentId)).willReturn(Optional.empty());

            // when
            Optional<RefreshEntity> result = refreshRepository.findById(nonExistentId);

            // then
            assertThat(result).isEmpty();
            verify(refreshRepository).findById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("리프레시 토큰 전체 삭제")
    class DeleteAll {

        @Test
        @DisplayName("성공: 모든 리프레시 토큰을 삭제한다")
        void deleteAll_Success() {
            // given
            doNothing().when(refreshRepository).deleteAll();

            // when
            refreshRepository.deleteAll();

            // then
            verify(refreshRepository, times(1)).deleteAll();
        }
    }

    @Nested
    @DisplayName("토큰 갱신 시나리오")
    class TokenRotationScenario {

        @Test
        @DisplayName("성공: 토큰 갱신 시 기존 토큰 삭제 후 새 토큰 저장")
        void tokenRotation_DeleteOldAndSaveNew_Success() {
            // given
            String oldToken = "old-refresh-token";
            String newToken = "new-refresh-token";

            RefreshEntity newEntity = new RefreshEntity(userEmail, newToken, "2025-12-31T23:59:59");
            RefreshEntity savedEntity = new RefreshEntity(userEmail, newToken, "2025-12-31T23:59:59");
            ReflectionTestUtils.setField(savedEntity, "id", 2L);

            given(refreshRepository.existsByRefresh(oldToken)).willReturn(true);
            doNothing().when(refreshRepository).deleteByRefresh(oldToken);
            given(refreshRepository.save(any(RefreshEntity.class))).willReturn(savedEntity);

            // when
            // 1. 기존 토큰 존재 확인
            Boolean oldTokenExists = refreshRepository.existsByRefresh(oldToken);

            // 2. 기존 토큰 삭제
            refreshRepository.deleteByRefresh(oldToken);

            // 3. 새 토큰 저장
            RefreshEntity result = refreshRepository.save(newEntity);

            // then
            assertThat(oldTokenExists).isTrue();
            assertThat(result).isNotNull();
            String storedToken = ReflectionTestUtils.getField(result, "refresh").toString();
            assertThat(storedToken).isEqualTo(newToken);

            verify(refreshRepository).existsByRefresh(oldToken);
            verify(refreshRepository).deleteByRefresh(oldToken);
            verify(refreshRepository).save(any(RefreshEntity.class));
        }

        @Test
        @DisplayName("성공: 로그아웃 시 토큰 삭제")
        void logout_DeleteToken_Success() {
            // given
            given(refreshRepository.existsByRefresh(refreshToken)).willReturn(true, false);
            doNothing().when(refreshRepository).deleteByRefresh(refreshToken);

            // when
            // 1. 토큰 존재 확인
            Boolean exists = refreshRepository.existsByRefresh(refreshToken);
            assertThat(exists).isTrue();

            // 2. 토큰 삭제 (로그아웃)
            refreshRepository.deleteByRefresh(refreshToken);

            // 3. 삭제 후 확인
            Boolean existsAfterLogout = refreshRepository.existsByRefresh(refreshToken);
            assertThat(existsAfterLogout).isFalse();

            // then
            verify(refreshRepository, times(2)).existsByRefresh(refreshToken);
            verify(refreshRepository).deleteByRefresh(refreshToken);
        }
    }

    @Nested
    @DisplayName("엔티티 ID로 삭제")
    class DeleteById {

        @Test
        @DisplayName("성공: ID로 리프레시 토큰을 삭제한다")
        void deleteById_Success() {
            // given
            Long id = 1L;
            doNothing().when(refreshRepository).deleteById(id);

            // when
            refreshRepository.deleteById(id);

            // then
            verify(refreshRepository, times(1)).deleteById(id);
        }

        @Test
        @DisplayName("성공: 존재하지 않는 ID 삭제 시 예외 없이 처리된다")
        void deleteById_NonExistent_NoException() {
            // given
            Long nonExistentId = 999L;
            doNothing().when(refreshRepository).deleteById(nonExistentId);

            // when
            refreshRepository.deleteById(nonExistentId);

            // then
            verify(refreshRepository).deleteById(nonExistentId);
        }
    }

    @Nested
    @DisplayName("토큰 개수 조회")
    class CountTokens {

        @Test
        @DisplayName("성공: 전체 토큰 개수를 조회한다")
        void count_ReturnsCorrectCount() {
            // given
            given(refreshRepository.count()).willReturn(5L);

            // when
            long count = refreshRepository.count();

            // then
            assertThat(count).isEqualTo(5L);
            verify(refreshRepository).count();
        }

        @Test
        @DisplayName("성공: 토큰이 없을 경우 0을 반환한다")
        void count_NoTokens_ReturnsZero() {
            // given
            given(refreshRepository.count()).willReturn(0L);

            // when
            long count = refreshRepository.count();

            // then
            assertThat(count).isEqualTo(0L);
            verify(refreshRepository).count();
        }
    }
}

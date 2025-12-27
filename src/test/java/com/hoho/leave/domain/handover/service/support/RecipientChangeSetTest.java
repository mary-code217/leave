package com.hoho.leave.domain.handover.service.support;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RecipientChangeSet 테스트")
class RecipientChangeSetTest {

    @Nested
    @DisplayName("변경 집합 생성")
    class CreateChangeSet {

        @Test
        @DisplayName("성공: 기존 ID와 원하는 ID로 변경 집합을 생성한다")
        void of_Success() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(2L, 3L, 4L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("성공: 빈 집합으로 생성한다")
        void of_EmptySets() {
            // given
            Set<Long> existingIds = Set.of();
            Set<Long> desiredIds = Set.of();

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getToAdd()).isEmpty();
            assertThat(result.getToRemove()).isEmpty();
        }
    }

    @Nested
    @DisplayName("추가 대상 계산")
    class GetToAdd {

        @Test
        @DisplayName("성공: 새로 추가해야 할 ID를 반환한다")
        void getToAdd_Success() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(2L, 3L, 4L, 5L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).containsExactlyInAnyOrder(4L, 5L);
        }

        @Test
        @DisplayName("성공: 기존 집합이 비어있으면 원하는 모든 ID를 반환한다")
        void getToAdd_EmptyExisting() {
            // given
            Set<Long> existingIds = Set.of();
            Set<Long> desiredIds = Set.of(1L, 2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).containsExactlyInAnyOrder(1L, 2L, 3L);
        }

        @Test
        @DisplayName("성공: 모든 ID가 이미 존재하면 빈 집합을 반환한다")
        void getToAdd_AllExist() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(1L, 2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).isEmpty();
        }

        @Test
        @DisplayName("성공: 원하는 ID가 비어있으면 빈 집합을 반환한다")
        void getToAdd_EmptyDesired() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of();

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).isEmpty();
        }
    }

    @Nested
    @DisplayName("삭제 대상 계산")
    class GetToRemove {

        @Test
        @DisplayName("성공: 삭제해야 할 ID를 반환한다")
        void getToRemove_Success() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L, 4L);
            Set<Long> desiredIds = Set.of(2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToRemove()).containsExactlyInAnyOrder(1L, 4L);
        }

        @Test
        @DisplayName("성공: 원하는 집합이 비어있으면 모든 기존 ID를 반환한다")
        void getToRemove_EmptyDesired() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of();

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToRemove()).containsExactlyInAnyOrder(1L, 2L, 3L);
        }

        @Test
        @DisplayName("성공: 모든 ID가 유지되면 빈 집합을 반환한다")
        void getToRemove_AllKeep() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L);
            Set<Long> desiredIds = Set.of(1L, 2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToRemove()).isEmpty();
        }

        @Test
        @DisplayName("성공: 기존 ID가 비어있으면 빈 집합을 반환한다")
        void getToRemove_EmptyExisting() {
            // given
            Set<Long> existingIds = Set.of();
            Set<Long> desiredIds = Set.of(1L, 2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToRemove()).isEmpty();
        }
    }

    @Nested
    @DisplayName("복합 시나리오")
    class ComplexScenarios {

        @Test
        @DisplayName("성공: 추가와 삭제가 동시에 발생하는 경우")
        void addAndRemove_Simultaneously() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(2L, 4L, 5L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).containsExactlyInAnyOrder(4L, 5L);
            assertThat(result.getToRemove()).containsExactlyInAnyOrder(1L, 3L);
        }

        @Test
        @DisplayName("성공: 완전히 새로운 집합으로 교체하는 경우")
        void completeReplacement() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(4L, 5L, 6L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).containsExactlyInAnyOrder(4L, 5L, 6L);
            assertThat(result.getToRemove()).containsExactlyInAnyOrder(1L, 2L, 3L);
        }

        @Test
        @DisplayName("성공: 단일 ID 추가")
        void singleAddition() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L);
            Set<Long> desiredIds = Set.of(1L, 2L, 3L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).containsExactly(3L);
            assertThat(result.getToRemove()).isEmpty();
        }

        @Test
        @DisplayName("성공: 단일 ID 삭제")
        void singleRemoval() {
            // given
            Set<Long> existingIds = Set.of(1L, 2L, 3L);
            Set<Long> desiredIds = Set.of(1L, 2L);

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);

            // then
            assertThat(result.getToAdd()).isEmpty();
            assertThat(result.getToRemove()).containsExactly(3L);
        }

        @Test
        @DisplayName("성공: 원본 집합이 수정되지 않는다")
        void originalSetsUnmodified() {
            // given
            Set<Long> existingIds = new HashSet<>(Set.of(1L, 2L, 3L));
            Set<Long> desiredIds = new HashSet<>(Set.of(2L, 3L, 4L));

            // when
            RecipientChangeSet result = RecipientChangeSet.of(existingIds, desiredIds);
            result.getToAdd();
            result.getToRemove();

            // then
            assertThat(existingIds).containsExactlyInAnyOrder(1L, 2L, 3L);
            assertThat(desiredIds).containsExactlyInAnyOrder(2L, 3L, 4L);
        }
    }
}

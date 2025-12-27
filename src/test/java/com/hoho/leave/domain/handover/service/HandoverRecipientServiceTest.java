package com.hoho.leave.domain.handover.service;

import com.hoho.leave.domain.handover.dto.response.HandoverRecipientListResponse;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.entity.HandoverRecipient;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.handover.service.support.RecipientChangeSet;
import com.hoho.leave.domain.user.entity.User;
import com.hoho.leave.domain.user.entity.UserRole;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HandoverRecipientService 테스트")
class HandoverRecipientServiceTest {

    @InjectMocks
    private HandoverRecipientService handoverRecipientService;

    @Mock
    private HandoverRecipientRepository repository;

    private User mockAuthor;
    private User mockRecipient1;
    private User mockRecipient2;
    private HandoverNote mockHandoverNote;
    private HandoverRecipient mockHandoverRecipient1;
    private HandoverRecipient mockHandoverRecipient2;

    @BeforeEach
    void setUp() {
        // Mock Author 생성
        mockAuthor = new User("author@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockAuthor, "id", 1L);
        ReflectionTestUtils.setField(mockAuthor, "username", "작성자");

        // Mock Recipient 생성
        mockRecipient1 = new User("recipient1@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockRecipient1, "id", 2L);
        ReflectionTestUtils.setField(mockRecipient1, "username", "수신자1");

        mockRecipient2 = new User("recipient2@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockRecipient2, "id", 3L);
        ReflectionTestUtils.setField(mockRecipient2, "username", "수신자2");

        // Mock HandoverNote 생성
        mockHandoverNote = HandoverNote.create(mockAuthor, "인수인계 제목", "인수인계 내용입니다.");
        ReflectionTestUtils.setField(mockHandoverNote, "id", 1L);
        ReflectionTestUtils.setField(mockHandoverNote, "createdAt", LocalDateTime.now());

        // Mock HandoverRecipient 생성
        mockHandoverRecipient1 = HandoverRecipient.create(mockHandoverNote, mockRecipient1);
        ReflectionTestUtils.setField(mockHandoverRecipient1, "id", 1L);
        ReflectionTestUtils.setField(mockHandoverRecipient1, "createdAt", LocalDateTime.now());

        mockHandoverRecipient2 = HandoverRecipient.create(mockHandoverNote, mockRecipient2);
        ReflectionTestUtils.setField(mockHandoverRecipient2, "id", 2L);
        ReflectionTestUtils.setField(mockHandoverRecipient2, "createdAt", LocalDateTime.now());
    }

    @Nested
    @DisplayName("수신자 생성")
    class CreateRecipients {

        @Test
        @DisplayName("성공: 수신자 목록을 생성한다")
        void createRecipients_Success() {
            // given
            List<User> recipients = List.of(mockRecipient1, mockRecipient2);
            List<HandoverRecipient> savedRecipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);

            given(repository.saveAll(anyCollection())).willReturn(savedRecipients);

            // when
            List<HandoverRecipient> result = handoverRecipientService.createRecipients(recipients, mockHandoverNote);

            // then
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            verify(repository, times(1)).saveAll(anyCollection());
        }

        @Test
        @DisplayName("성공: 빈 수신자 목록으로 생성 시 빈 리스트 반환")
        void createRecipients_EmptyList() {
            // given
            List<User> emptyRecipients = List.of();
            given(repository.saveAll(anyCollection())).willReturn(List.of());

            // when
            List<HandoverRecipient> result = handoverRecipientService.createRecipients(emptyRecipients, mockHandoverNote);

            // then
            assertThat(result).isEmpty();
            verify(repository, times(1)).saveAll(anyCollection());
        }

        @Test
        @DisplayName("성공: 단일 수신자를 생성한다")
        void createRecipients_SingleRecipient() {
            // given
            List<User> recipients = List.of(mockRecipient1);
            List<HandoverRecipient> savedRecipients = List.of(mockHandoverRecipient1);

            given(repository.saveAll(anyCollection())).willReturn(savedRecipients);

            // when
            List<HandoverRecipient> result = handoverRecipientService.createRecipients(recipients, mockHandoverNote);

            // then
            assertThat(result).hasSize(1);
            verify(repository, times(1)).saveAll(anyCollection());
        }
    }

    @Nested
    @DisplayName("수신 목록 조회")
    class GetRecipientList {

        @Test
        @DisplayName("성공: 수신자의 수신 인수인계 목록을 페이지네이션하여 조회한다")
        void getRecipientList_Success() {
            // given
            Long recipientId = 2L;
            Integer page = 1;
            Integer size = 10;

            List<HandoverRecipient> recipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);
            Page<HandoverRecipient> recipientPage = new PageImpl<>(recipients);

            given(repository.findByRecipientId(eq(recipientId), any(Pageable.class)))
                    .willReturn(recipientPage);

            // when
            HandoverRecipientListResponse response = handoverRecipientService.getRecipientList(recipientId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getRecipients()).hasSize(2);
            assertThat(response.getPage()).isEqualTo(1);
            verify(repository).findByRecipientId(eq(recipientId), any(Pageable.class));
        }

        @Test
        @DisplayName("성공: 수신 인수인계가 없는 경우 빈 목록을 반환한다")
        void getRecipientList_EmptyList() {
            // given
            Long recipientId = 2L;
            Integer page = 1;
            Integer size = 10;

            Page<HandoverRecipient> emptyPage = new PageImpl<>(List.of());
            given(repository.findByRecipientId(eq(recipientId), any(Pageable.class)))
                    .willReturn(emptyPage);

            // when
            HandoverRecipientListResponse response = handoverRecipientService.getRecipientList(recipientId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getRecipients()).isEmpty();
            assertThat(response.getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지 정보가 올바르게 반환된다")
        void getRecipientList_PaginationInfo() {
            // given
            Long recipientId = 2L;
            Integer page = 2;
            Integer size = 5;

            List<HandoverRecipient> recipients = List.of(mockHandoverRecipient1);
            Page<HandoverRecipient> recipientPage = new PageImpl<>(recipients);

            given(repository.findByRecipientId(eq(recipientId), any(Pageable.class)))
                    .willReturn(recipientPage);

            // when
            HandoverRecipientListResponse response = handoverRecipientService.getRecipientList(recipientId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getRecipients()).hasSize(1);
            verify(repository).findByRecipientId(eq(recipientId), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("수신자 삭제")
    class DeleteRecipients {

        @Test
        @DisplayName("성공: 인수인계 노트의 모든 수신자를 삭제한다")
        void deleteByHandoverNoteId_Success() {
            // given
            Long handoverNoteId = 1L;

            // when
            handoverRecipientService.deleteByHandoverNoteId(handoverNoteId);

            // then
            verify(repository, times(1)).deleteByHandoverNoteId(handoverNoteId);
        }

        @Test
        @DisplayName("성공: 특정 수신자들을 삭제한다")
        void deleteByHandoverIdAndRecipientIds_Success() {
            // given
            Long handoverId = 1L;
            Collection<Long> recipientIds = List.of(2L, 3L);

            // when
            handoverRecipientService.deleteByHandoverIdAndRecipientIds(handoverId, recipientIds);

            // then
            verify(repository, times(1)).deleteByNoteIdAndRecipientIds(handoverId, recipientIds);
        }
    }

    @Nested
    @DisplayName("수신자 조회")
    class FindRecipients {

        @Test
        @DisplayName("성공: 인수인계 노트의 모든 수신자를 조회한다")
        void findAllByHandoverNoteId_Success() {
            // given
            Long handoverNoteId = 1L;
            List<HandoverRecipient> recipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);

            given(repository.findAllByHandoverNoteId(handoverNoteId)).willReturn(recipients);

            // when
            List<HandoverRecipient> result = handoverRecipientService.findAllByHandoverNoteId(handoverNoteId);

            // then
            assertThat(result).hasSize(2);
            verify(repository, times(1)).findAllByHandoverNoteId(handoverNoteId);
        }

        @Test
        @DisplayName("성공: 수신자가 없는 경우 빈 리스트를 반환한다")
        void findAllByHandoverNoteId_EmptyList() {
            // given
            Long handoverNoteId = 999L;

            given(repository.findAllByHandoverNoteId(handoverNoteId)).willReturn(List.of());

            // when
            List<HandoverRecipient> result = handoverRecipientService.findAllByHandoverNoteId(handoverNoteId);

            // then
            assertThat(result).isEmpty();
            verify(repository, times(1)).findAllByHandoverNoteId(handoverNoteId);
        }
    }

    @Nested
    @DisplayName("수신자 변경 계산")
    class AddAndDeleteRecipients {

        @Test
        @DisplayName("성공: 추가/삭제 대상 수신자를 계산한다")
        void addAndDeleteRecipients_Success() {
            // given
            List<HandoverRecipient> existingRecipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);
            List<Long> newRecipientIds = List.of(2L, 4L); // 2L 유지, 3L 삭제, 4L 추가

            // when
            RecipientChangeSet result = handoverRecipientService.addAndDeleteRecipients(existingRecipients, newRecipientIds);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getToAdd()).containsExactly(4L);
            assertThat(result.getToRemove()).containsExactly(3L);
        }

        @Test
        @DisplayName("성공: 모든 수신자가 새로 추가될 경우")
        void addAndDeleteRecipients_AllNew() {
            // given
            List<HandoverRecipient> existingRecipients = List.of();
            List<Long> newRecipientIds = List.of(2L, 3L);

            // when
            RecipientChangeSet result = handoverRecipientService.addAndDeleteRecipients(existingRecipients, newRecipientIds);

            // then
            assertThat(result.getToAdd()).containsExactlyInAnyOrder(2L, 3L);
            assertThat(result.getToRemove()).isEmpty();
        }

        @Test
        @DisplayName("성공: 모든 수신자가 삭제될 경우")
        void addAndDeleteRecipients_AllRemoved() {
            // given
            List<HandoverRecipient> existingRecipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);
            List<Long> newRecipientIds = List.of();

            // when
            RecipientChangeSet result = handoverRecipientService.addAndDeleteRecipients(existingRecipients, newRecipientIds);

            // then
            assertThat(result.getToAdd()).isEmpty();
            assertThat(result.getToRemove()).containsExactlyInAnyOrder(2L, 3L);
        }

        @Test
        @DisplayName("성공: 변경이 없는 경우")
        void addAndDeleteRecipients_NoChange() {
            // given
            List<HandoverRecipient> existingRecipients = List.of(mockHandoverRecipient1, mockHandoverRecipient2);
            List<Long> newRecipientIds = List.of(2L, 3L); // 동일

            // when
            RecipientChangeSet result = handoverRecipientService.addAndDeleteRecipients(existingRecipients, newRecipientIds);

            // then
            assertThat(result.getToAdd()).isEmpty();
            assertThat(result.getToRemove()).isEmpty();
        }
    }
}

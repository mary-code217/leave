package com.hoho.leave.domain.handover.service;

import com.hoho.leave.common.exception.NotFoundException;
import com.hoho.leave.domain.handover.dto.response.HandoverAuthorListResponse;
import com.hoho.leave.domain.handover.dto.response.HandoverDetailResponse;
import com.hoho.leave.domain.handover.entity.HandoverNote;
import com.hoho.leave.domain.handover.repository.HandoverNoteRepository;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository;
import com.hoho.leave.domain.handover.repository.HandoverRecipientRepository.RecipientUsernameRow;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("HandoverService 테스트")
class HandoverServiceTest {

    @InjectMocks
    private HandoverService handoverService;

    @Mock
    private HandoverNoteRepository noteRepository;

    @Mock
    private HandoverRecipientRepository recipientRepository;

    private User mockAuthor;
    private User mockRecipient;
    private HandoverNote mockHandoverNote;

    @BeforeEach
    void setUp() {
        // Mock Author 생성
        mockAuthor = new User("author@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockAuthor, "id", 1L);
        ReflectionTestUtils.setField(mockAuthor, "username", "작성자");

        // Mock Recipient 생성
        mockRecipient = new User("recipient@example.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(mockRecipient, "id", 2L);
        ReflectionTestUtils.setField(mockRecipient, "username", "수신자");

        // Mock HandoverNote 생성
        mockHandoverNote = HandoverNote.create(mockAuthor, "인수인계 제목", "인수인계 내용입니다.");
        ReflectionTestUtils.setField(mockHandoverNote, "id", 1L);
        ReflectionTestUtils.setField(mockHandoverNote, "createdAt", LocalDateTime.now());
    }

    @Nested
    @DisplayName("인수인계 생성")
    class CreateHandover {

        @Test
        @DisplayName("성공: 인수인계 노트를 생성한다")
        void createHandover_Success() {
            // given
            given(noteRepository.save(any(HandoverNote.class))).willReturn(mockHandoverNote);

            // when
            HandoverNote result = handoverService.createHandover(mockAuthor, "인수인계 제목", "인수인계 내용입니다.");

            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("인수인계 제목");
            assertThat(result.getContent()).isEqualTo("인수인계 내용입니다.");
            assertThat(result.getAuthor()).isEqualTo(mockAuthor);
            verify(noteRepository, times(1)).save(any(HandoverNote.class));
        }
    }

    @Nested
    @DisplayName("인수인계 수정")
    class UpdateHandover {

        @Test
        @DisplayName("성공: 인수인계 노트를 수정한다")
        void updateHandover_Success() {
            // given
            Long handoverId = 1L;
            String newTitle = "수정된 제목";
            String newContent = "수정된 내용입니다.";
            given(noteRepository.findById(handoverId)).willReturn(Optional.of(mockHandoverNote));

            // when
            HandoverNote result = handoverService.updateHandover(handoverId, newTitle, newContent);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo(newTitle);
            assertThat(result.getContent()).isEqualTo(newContent);
            verify(noteRepository).findById(handoverId);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 인수인계 수정 시 예외 발생")
        void updateHandover_NotFound_ThrowsException() {
            // given
            Long handoverId = 999L;
            given(noteRepository.findById(handoverId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> handoverService.updateHandover(handoverId, "제목", "내용"))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found HandoverNote");

            verify(noteRepository).findById(handoverId);
        }
    }

    @Nested
    @DisplayName("인수인계 삭제")
    class DeleteHandover {

        @Test
        @DisplayName("성공: 인수인계 노트를 삭제한다")
        void deleteHandover_Success() {
            // given
            Long handoverId = 1L;
            given(noteRepository.findById(handoverId)).willReturn(Optional.of(mockHandoverNote));

            // when
            handoverService.deleteHandover(handoverId);

            // then
            verify(noteRepository).findById(handoverId);
            verify(noteRepository).delete(mockHandoverNote);
        }

        @Test
        @DisplayName("실패: 존재하지 않는 인수인계 삭제 시 예외 발생")
        void deleteHandover_NotFound_ThrowsException() {
            // given
            Long handoverId = 999L;
            given(noteRepository.findById(handoverId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> handoverService.deleteHandover(handoverId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found HandoverNote");

            verify(noteRepository).findById(handoverId);
            verify(noteRepository, never()).delete(any(HandoverNote.class));
        }
    }

    @Nested
    @DisplayName("작성자의 발신 인수인계 목록 조회")
    class GetHandoverAuthorList {

        @Test
        @DisplayName("성공: 작성자의 발신 인수인계 목록을 페이지네이션하여 조회한다")
        void getHandoverAuthorList_Success() {
            // given
            Long authorId = 1L;
            Integer page = 1;
            Integer size = 10;

            HandoverNote handoverNote2 = HandoverNote.create(mockAuthor, "인수인계 제목2", "인수인계 내용2입니다.");
            ReflectionTestUtils.setField(handoverNote2, "id", 2L);
            ReflectionTestUtils.setField(handoverNote2, "createdAt", LocalDateTime.now().minusHours(1));

            List<HandoverNote> handoverNotes = List.of(mockHandoverNote, handoverNote2);
            Page<HandoverNote> handoverPage = new PageImpl<>(handoverNotes);

            // Mock RecipientUsernameRow
            RecipientUsernameRow row1 = mock(RecipientUsernameRow.class);
            given(row1.getNoteId()).willReturn(1L);
            given(row1.getUsername()).willReturn("수신자1");

            RecipientUsernameRow row2 = mock(RecipientUsernameRow.class);
            given(row2.getNoteId()).willReturn(2L);
            given(row2.getUsername()).willReturn("수신자2");

            given(noteRepository.findByAuthorId(eq(authorId), any(Pageable.class)))
                    .willReturn(handoverPage);
            given(recipientRepository.findRecipientUsernamesByNoteIds(anyCollection()))
                    .willReturn(List.of(row1, row2));

            // when
            HandoverAuthorListResponse response = handoverService.getHandoverAuthorList(authorId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getHandoverNotes()).hasSize(2);
            assertThat(response.getPage()).isEqualTo(1);
            verify(noteRepository).findByAuthorId(eq(authorId), any(Pageable.class));
            verify(recipientRepository).findRecipientUsernamesByNoteIds(anyCollection());
        }

        @Test
        @DisplayName("성공: 인수인계가 없는 경우 빈 목록을 반환한다")
        void getHandoverAuthorList_EmptyList() {
            // given
            Long authorId = 1L;
            Integer page = 1;
            Integer size = 10;

            Page<HandoverNote> emptyPage = new PageImpl<>(List.of());
            given(noteRepository.findByAuthorId(eq(authorId), any(Pageable.class)))
                    .willReturn(emptyPage);
            given(recipientRepository.findRecipientUsernamesByNoteIds(anyCollection()))
                    .willReturn(List.of());

            // when
            HandoverAuthorListResponse response = handoverService.getHandoverAuthorList(authorId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getHandoverNotes()).isEmpty();
            assertThat(response.getTotalElement()).isEqualTo(0L);
        }

        @Test
        @DisplayName("성공: 페이지 정보가 올바르게 반환된다")
        void getHandoverAuthorList_PaginationInfo() {
            // given
            Long authorId = 1L;
            Integer page = 2;
            Integer size = 5;

            List<HandoverNote> handoverNotes = List.of(mockHandoverNote);
            Page<HandoverNote> handoverPage = new PageImpl<>(handoverNotes);

            given(noteRepository.findByAuthorId(eq(authorId), any(Pageable.class)))
                    .willReturn(handoverPage);
            given(recipientRepository.findRecipientUsernamesByNoteIds(anyCollection()))
                    .willReturn(List.of());

            // when
            HandoverAuthorListResponse response = handoverService.getHandoverAuthorList(authorId, page, size);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getHandoverNotes()).hasSize(1);
            verify(noteRepository).findByAuthorId(eq(authorId), any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("인수인계 단건 조회")
    class GetHandover {

        @Test
        @DisplayName("성공: 인수인계를 상세 조회한다")
        void getHandover_Success() {
            // given
            Long handoverId = 1L;
            List<String> recipientNames = List.of("수신자1", "수신자2");

            given(noteRepository.findById(handoverId)).willReturn(Optional.of(mockHandoverNote));
            given(recipientRepository.findRecipientNamesByHandoverNoteId(handoverId))
                    .willReturn(recipientNames);

            // when
            HandoverDetailResponse response = handoverService.getHandover(handoverId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getHandoverId()).isEqualTo(handoverId);
            assertThat(response.getTitle()).isEqualTo("인수인계 제목");
            assertThat(response.getContent()).isEqualTo("인수인계 내용입니다.");
            assertThat(response.getAuthorName()).isEqualTo("작성자");
            assertThat(response.getRecipientNames()).hasSize(2);
            assertThat(response.getRecipientNames()).containsExactly("수신자1", "수신자2");
            verify(noteRepository).findById(handoverId);
            verify(recipientRepository).findRecipientNamesByHandoverNoteId(handoverId);
        }

        @Test
        @DisplayName("성공: 수신자가 없는 인수인계를 조회한다")
        void getHandover_NoRecipients() {
            // given
            Long handoverId = 1L;
            List<String> emptyRecipientNames = List.of();

            given(noteRepository.findById(handoverId)).willReturn(Optional.of(mockHandoverNote));
            given(recipientRepository.findRecipientNamesByHandoverNoteId(handoverId))
                    .willReturn(emptyRecipientNames);

            // when
            HandoverDetailResponse response = handoverService.getHandover(handoverId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getRecipientNames()).isEmpty();
        }

        @Test
        @DisplayName("실패: 존재하지 않는 인수인계 조회 시 예외 발생")
        void getHandover_NotFound_ThrowsException() {
            // given
            Long handoverId = 999L;
            given(noteRepository.findById(handoverId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> handoverService.getHandover(handoverId))
                    .isInstanceOf(NotFoundException.class)
                    .hasMessageContaining("Not Found HandoverNote");

            verify(noteRepository).findById(handoverId);
            verify(recipientRepository, never()).findRecipientNamesByHandoverNoteId(any());
        }
    }
}

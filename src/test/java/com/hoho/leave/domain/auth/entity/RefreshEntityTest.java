package com.hoho.leave.domain.auth.entity;

import com.hoho.leave.domain.auth.RefreshEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RefreshEntity 테스트")
class RefreshEntityTest {

    private RefreshEntity refreshEntity;
    private String userEmail;
    private String refresh;
    private String expiration;

    @BeforeEach
    void setUp() {
        userEmail = "test@example.com";
        refresh = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh.token";
        expiration = "2025-12-30T10:00:00";

        refreshEntity = new RefreshEntity(userEmail, refresh, expiration);
    }

    @Nested
    @DisplayName("엔티티 생성")
    class CreateEntity {

        @Test
        @DisplayName("성공: 3개 파라미터 생성자로 RefreshEntity를 생성한다")
        void createRefreshEntity_WithThreeParams_Success() {
            // given
            String email = "user@example.com";
            String refreshToken = "refresh-token-value";
            String exp = "2025-12-31T23:59:59";

            // when
            RefreshEntity entity = new RefreshEntity(email, refreshToken, exp);

            // then
            assertThat(entity).isNotNull();
            String storedEmail = ReflectionTestUtils.getField(entity, "userEmail").toString();
            String storedRefresh = ReflectionTestUtils.getField(entity, "refresh").toString();
            String storedExpiration = ReflectionTestUtils.getField(entity, "expiration").toString();

            assertThat(storedEmail).isEqualTo(email);
            assertThat(storedRefresh).isEqualTo(refreshToken);
            assertThat(storedExpiration).isEqualTo(exp);
        }

        @Test
        @DisplayName("성공: 4개 파라미터 생성자(id 포함)로 RefreshEntity를 생성한다")
        void createRefreshEntity_WithAllParams_Success() {
            // given
            Long id = 1L;
            String email = "admin@example.com";
            String refreshToken = "admin-refresh-token";
            String exp = "2026-01-01T00:00:00";

            // when
            RefreshEntity entity = new RefreshEntity(id, email, refreshToken, exp);

            // then
            assertThat(entity).isNotNull();
            Long storedId = (Long) ReflectionTestUtils.getField(entity, "id");
            String storedEmail = ReflectionTestUtils.getField(entity, "userEmail").toString();
            String storedRefresh = ReflectionTestUtils.getField(entity, "refresh").toString();
            String storedExpiration = ReflectionTestUtils.getField(entity, "expiration").toString();

            assertThat(storedId).isEqualTo(id);
            assertThat(storedEmail).isEqualTo(email);
            assertThat(storedRefresh).isEqualTo(refreshToken);
            assertThat(storedExpiration).isEqualTo(exp);
        }

        @Test
        @DisplayName("성공: null 값으로도 엔티티 생성이 가능하다")
        void createRefreshEntity_WithNullValues_Success() {
            // given & when
            RefreshEntity entity = new RefreshEntity(null, null, null);

            // then
            assertThat(entity).isNotNull();
            assertThat(ReflectionTestUtils.getField(entity, "userEmail")).isNull();
            assertThat(ReflectionTestUtils.getField(entity, "refresh")).isNull();
            assertThat(ReflectionTestUtils.getField(entity, "expiration")).isNull();
        }
    }

    @Nested
    @DisplayName("필드 검증")
    class FieldValidation {

        @Test
        @DisplayName("성공: userEmail 필드가 올바르게 저장된다")
        void userEmail_StoredCorrectly() {
            // given
            String expectedEmail = "test@example.com";

            // when
            String actualEmail = ReflectionTestUtils.getField(refreshEntity, "userEmail").toString();

            // then
            assertThat(actualEmail).isEqualTo(expectedEmail);
        }

        @Test
        @DisplayName("성공: refresh 필드가 올바르게 저장된다")
        void refresh_StoredCorrectly() {
            // given
            String expectedRefresh = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.refresh.token";

            // when
            String actualRefresh = ReflectionTestUtils.getField(refreshEntity, "refresh").toString();

            // then
            assertThat(actualRefresh).isEqualTo(expectedRefresh);
        }

        @Test
        @DisplayName("성공: expiration 필드가 올바르게 저장된다")
        void expiration_StoredCorrectly() {
            // given
            String expectedExpiration = "2025-12-30T10:00:00";

            // when
            String actualExpiration = ReflectionTestUtils.getField(refreshEntity, "expiration").toString();

            // then
            assertThat(actualExpiration).isEqualTo(expectedExpiration);
        }

        @Test
        @DisplayName("성공: id 필드는 초기값이 null이다")
        void id_InitiallyNull() {
            // when
            Object id = ReflectionTestUtils.getField(refreshEntity, "id");

            // then
            assertThat(id).isNull();
        }

        @Test
        @DisplayName("성공: ReflectionTestUtils로 id를 설정할 수 있다")
        void id_CanBeSetWithReflection() {
            // given
            Long expectedId = 100L;

            // when
            ReflectionTestUtils.setField(refreshEntity, "id", expectedId);
            Long actualId = (Long) ReflectionTestUtils.getField(refreshEntity, "id");

            // then
            assertThat(actualId).isEqualTo(expectedId);
        }
    }

    @Nested
    @DisplayName("다양한 토큰 형식")
    class VariousTokenFormats {

        @Test
        @DisplayName("성공: JWT 형식의 리프레시 토큰을 저장한다")
        void jwtFormatToken_Success() {
            // given
            String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", jwtToken, "2025-12-31T23:59:59");

            // then
            String storedToken = ReflectionTestUtils.getField(entity, "refresh").toString();
            assertThat(storedToken).isEqualTo(jwtToken);
            assertThat(storedToken).contains(".");
        }

        @Test
        @DisplayName("성공: UUID 형식의 리프레시 토큰을 저장한다")
        void uuidFormatToken_Success() {
            // given
            String uuidToken = "550e8400-e29b-41d4-a716-446655440000";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", uuidToken, "2025-12-31T23:59:59");

            // then
            String storedToken = ReflectionTestUtils.getField(entity, "refresh").toString();
            assertThat(storedToken).isEqualTo(uuidToken);
        }

        @Test
        @DisplayName("성공: 빈 문자열 토큰도 저장 가능하다")
        void emptyStringToken_Success() {
            // given
            String emptyToken = "";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", emptyToken, "2025-12-31T23:59:59");

            // then
            String storedToken = ReflectionTestUtils.getField(entity, "refresh").toString();
            assertThat(storedToken).isEmpty();
        }

        @Test
        @DisplayName("성공: 긴 토큰 문자열도 저장 가능하다")
        void longToken_Success() {
            // given
            String longToken = "a".repeat(1000);

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", longToken, "2025-12-31T23:59:59");

            // then
            String storedToken = ReflectionTestUtils.getField(entity, "refresh").toString();
            assertThat(storedToken).hasSize(1000);
        }
    }

    @Nested
    @DisplayName("만료 시간 형식")
    class ExpirationFormat {

        @Test
        @DisplayName("성공: ISO-8601 형식의 만료 시간을 저장한다")
        void iso8601Format_Success() {
            // given
            String isoExpiration = "2025-12-31T23:59:59";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", "token", isoExpiration);

            // then
            String storedExpiration = ReflectionTestUtils.getField(entity, "expiration").toString();
            assertThat(storedExpiration).isEqualTo(isoExpiration);
        }

        @Test
        @DisplayName("성공: 타임스탬프 형식의 만료 시간을 저장한다")
        void timestampFormat_Success() {
            // given
            String timestampExpiration = "1735689599000";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", "token", timestampExpiration);

            // then
            String storedExpiration = ReflectionTestUtils.getField(entity, "expiration").toString();
            assertThat(storedExpiration).isEqualTo(timestampExpiration);
        }

        @Test
        @DisplayName("성공: 사용자 정의 형식의 만료 시간을 저장한다")
        void customFormat_Success() {
            // given
            String customExpiration = "2025/12/31 23:59:59";

            // when
            RefreshEntity entity = new RefreshEntity("user@test.com", "token", customExpiration);

            // then
            String storedExpiration = ReflectionTestUtils.getField(entity, "expiration").toString();
            assertThat(storedExpiration).isEqualTo(customExpiration);
        }
    }

    @Nested
    @DisplayName("이메일 형식")
    class EmailFormat {

        @Test
        @DisplayName("성공: 표준 이메일 형식을 저장한다")
        void standardEmail_Success() {
            // given
            String email = "user@company.com";

            // when
            RefreshEntity entity = new RefreshEntity(email, "token", "2025-12-31T23:59:59");

            // then
            String storedEmail = ReflectionTestUtils.getField(entity, "userEmail").toString();
            assertThat(storedEmail).isEqualTo(email);
            assertThat(storedEmail).contains("@");
        }

        @Test
        @DisplayName("성공: 서브도메인이 포함된 이메일을 저장한다")
        void subdomainEmail_Success() {
            // given
            String email = "user@mail.company.co.kr";

            // when
            RefreshEntity entity = new RefreshEntity(email, "token", "2025-12-31T23:59:59");

            // then
            String storedEmail = ReflectionTestUtils.getField(entity, "userEmail").toString();
            assertThat(storedEmail).isEqualTo(email);
        }

        @Test
        @DisplayName("성공: 플러스 태그가 포함된 이메일을 저장한다")
        void plusTagEmail_Success() {
            // given
            String email = "user+tag@company.com";

            // when
            RefreshEntity entity = new RefreshEntity(email, "token", "2025-12-31T23:59:59");

            // then
            String storedEmail = ReflectionTestUtils.getField(entity, "userEmail").toString();
            assertThat(storedEmail).isEqualTo(email);
            assertThat(storedEmail).contains("+");
        }
    }
}

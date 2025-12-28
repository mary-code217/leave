# JUnit 5 + Mockito 단위 테스트 가이드

이 문서는 audit 도메인 테스트 코드에서 사용된 주요 기능들을 정리한 것입니다.

---

## 1. 테스트 클래스 설정

### 1.1 필수 어노테이션

```java
@ExtendWith(MockitoExtension.class)  // Mockito 확장 활성화
@DisplayName("AuditLogService 테스트")  // 테스트 클래스 이름 (한글 가능)
class AuditLogServiceTest {
    // ...
}
```

| 어노테이션 | 설명 |
|-----------|------|
| `@ExtendWith(MockitoExtension.class)` | JUnit 5에서 Mockito 사용 |
| `@DisplayName` | 테스트 결과에 표시될 이름 |

---

## 2. Mock 객체 생성

### 2.1 @Mock vs @InjectMocks

```java
@InjectMocks
private AuditLogService auditLogService;  // 테스트 대상

@Mock
private AuditLogRepository auditLogRepository;  // 가짜 의존성

@Mock
private UserRepository userRepository;  // 가짜 의존성
```

| 어노테이션 | 용도 |
|-----------|------|
| `@Mock` | 가짜 객체 생성 (DB 연결 X, HTTP 요청 X) |
| `@InjectMocks` | Mock 객체들을 주입받는 테스트 대상 |

### 2.2 동작 원리

```
┌─────────────────────────────────────────────────────┐
│  AuditLogService (테스트 대상)                        │
│    ├── AuditLogRepository (Mock) ──▶ DB 연결 없음    │
│    └── UserRepository (Mock) ──▶ DB 연결 없음        │
└─────────────────────────────────────────────────────┘
```

---

## 3. 테스트 데이터 준비 (@BeforeEach)

### 3.1 @BeforeEach

각 테스트 메서드 실행 전에 자동 호출됩니다.

```java
@BeforeEach
void setUp() {
    // 모든 테스트 전에 실행되는 초기화 코드
    mockUser = new User("test@example.com", UserRole.ROLE_USER);
    // ...
}
```

### 3.2 ReflectionTestUtils

private 필드에 값을 주입할 때 사용합니다.

```java
// User 엔티티의 private id 필드에 값 설정
ReflectionTestUtils.setField(mockUser, "id", 1L);
ReflectionTestUtils.setField(mockUser, "username", "테스트유저");
ReflectionTestUtils.setField(mockUser, "employeeNo", "EMP001");
```

**왜 필요한가?**
- 엔티티의 `id`는 보통 `@GeneratedValue`로 DB가 생성
- 테스트에서는 DB 없이 직접 id를 설정해야 함
- private 필드는 직접 접근 불가 → ReflectionTestUtils 사용

---

## 4. 테스트 구조화 (@Nested)

### 4.1 @Nested 클래스

관련 테스트를 그룹화하여 가독성을 높입니다.

```java
@Nested
@DisplayName("감사 로그 생성")
class CreateLog {

    @Test
    @DisplayName("성공: 새로운 감사 로그를 생성한다")
    void createLog_Success() {
        // ...
    }

    @Test
    @DisplayName("성공: actorId가 null인 시스템 로그를 생성한다")
    void createLog_NullActorId_Success() {
        // ...
    }
}

@Nested
@DisplayName("감사 로그 목록 조회")
class GetAllLogs {
    // ...
}
```

**테스트 결과 출력:**
```
AuditLogService 테스트
├── 감사 로그 생성
│   ├── 성공: 새로운 감사 로그를 생성한다 ✓
│   └── 성공: actorId가 null인 시스템 로그를 생성한다 ✓
└── 감사 로그 목록 조회
    ├── 성공: 전체 감사 로그 목록을 페이징하여 조회한다 ✓
    └── ...
```

---

## 5. Given-When-Then 패턴

### 5.1 기본 구조

```java
@Test
@DisplayName("성공: 전체 감사 로그 목록을 조회한다")
void getAllLogs_Success() {
    // given - 테스트 준비 (Mock 동작 정의)
    Integer page = 1;
    Integer size = 10;
    String objectType = "all";

    given(auditLogRepository.findAll(any(Pageable.class)))
        .willReturn(auditLogPage);

    // when - 테스트 실행
    AuditLogListResponse response = auditLogService.getAllLogs(page, size, objectType);

    // then - 결과 검증
    assertThat(response).isNotNull();
    assertThat(response.getAuditLogs()).hasSize(2);
}
```

| 단계 | 역할 |
|------|------|
| **Given** | 테스트 사전 조건 설정, Mock 동작 정의 |
| **When** | 테스트 대상 메서드 실행 |
| **Then** | 결과 검증 (assert, verify) |

---

## 6. Mockito - Mock 동작 정의

### 6.1 given().willReturn()

"이 메서드가 호출되면, 이 값을 반환해라"

```java
// Repository.save() 호출 시 mockAuditLog 반환
given(auditLogRepository.save(any(AuditLog.class)))
    .willReturn(mockAuditLog);

// Repository.findAll() 호출 시 auditLogPage 반환
given(auditLogRepository.findAll(any(Pageable.class)))
    .willReturn(auditLogPage);
```

### 6.2 ArgumentMatchers (매개변수 매칭)

```java
any()                    // 모든 값
any(AuditLog.class)      // AuditLog 타입의 모든 값
any(Pageable.class)      // Pageable 타입의 모든 값
eq(objectType)           // 정확히 objectType과 같은 값
```

**혼합 사용 예시:**
```java
// objectType은 정확히 일치, Pageable은 아무 값이나
given(auditLogRepository.findAllByObjectType(eq(objectType), any(Pageable.class)))
    .willReturn(auditLogPage);
```

### 6.3 주의사항

```java
// ❌ 잘못된 사용 - eq()와 일반 값 혼합 불가
given(repository.find(eq(1L), "name")).willReturn(result);

// ✅ 올바른 사용 - 모두 Matcher 사용
given(repository.find(eq(1L), eq("name"))).willReturn(result);
```

---

## 7. 검증 (Assertions & Verify)

### 7.1 AssertJ - assertThat()

```java
// null 체크
assertThat(response).isNotNull();

// 값 비교
assertThat(response.getPage()).isEqualTo(1);

// 컬렉션 크기
assertThat(response.getAuditLogs()).hasSize(2);

// 빈 컬렉션
assertThat(response.getAuditLogs()).isEmpty();

// Boolean
assertThat(response.getFirstPage()).isTrue();
assertThat(response.getLastPage()).isFalse();

// HTTP 상태 코드
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
```

### 7.2 Mockito - verify()

"이 메서드가 호출되었는지 확인"

```java
// 1번 호출 확인
verify(auditLogRepository).save(any(AuditLog.class));

// 정확히 1번 호출 확인 (명시적)
verify(auditLogRepository, times(1)).save(any(AuditLog.class));

// 특정 파라미터로 호출 확인
verify(auditLogService).getAllLogs(page, size, objectType);
```

---

## 8. 페이징 테스트

### 8.1 PageImpl 사용

```java
// 가짜 Page 객체 생성
List<AuditLog> auditLogs = List.of(mockAuditLog, mockAuditLog2);
Page<AuditLog> auditLogPage = new PageImpl<>(auditLogs);

given(auditLogRepository.findAll(any(Pageable.class)))
    .willReturn(auditLogPage);
```

### 8.2 페이징 정보 검증

```java
assertThat(response.getPage()).isEqualTo(2);
assertThat(response.getSize()).isEqualTo(5);
assertThat(response.getTotalPage()).isEqualTo(3);
assertThat(response.getTotalElement()).isEqualTo(15L);
assertThat(response.getFirstPage()).isFalse();
assertThat(response.getLastPage()).isFalse();
```

---

## 9. Controller 테스트

### 9.1 Service를 Mock으로 대체

```java
@InjectMocks
private AuditLogController auditLogController;

@Mock
private AuditLogService auditLogService;  // HTTP 요청 없이 가짜 응답
```

### 9.2 ResponseEntity 검증

```java
// when
ResponseEntity<AuditLogListResponse> response =
    auditLogController.getAllLogs(page, size, objectType);

// then
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
assertThat(response.getBody()).isNotNull();
assertThat(response.getBody().getAuditLogs()).hasSize(2);
```

---

## 10. Stream API 활용

### 10.1 특정 조건 필터링 후 검증

```java
// 특정 Action을 가진 로그 찾기
AuditLogDetailResponse systemLog = response.getBody().getAuditLogs().stream()
    .filter(log -> log.getAction() == Action.AUDIT_LOG_PURGED)
    .findFirst()
    .orElse(null);

assertThat(systemLog).isNotNull();
assertThat(systemLog.getUsername()).isEqualTo("관리자");
```

---

## 11. 테스트 네이밍 컨벤션

### 11.1 메서드 이름

```java
// 패턴: 메서드명_상황_예상결과
void createLog_Success()
void createLog_NullActorId_Success()
void getAllLogs_EmptyList()
void getAllLogs_ByObjectType_Success()
void getAllLogs_WithDeletedUser_Success()
```

### 11.2 @DisplayName

```java
@DisplayName("성공: 새로운 감사 로그를 생성한다")
@DisplayName("성공: actorId가 null인 시스템 로그를 생성한다")
@DisplayName("성공: 감사 로그가 없는 경우 빈 목록을 반환한다")
```

---

## 12. Import 정리

### 12.1 필수 Import

```java
// JUnit 5
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

// AssertJ
import static org.assertj.core.api.Assertions.assertThat;

// Spring Test
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
```

---

## 13. 요약 체크리스트

### 테스트 작성 시 확인사항

- [ ] `@ExtendWith(MockitoExtension.class)` 추가
- [ ] 테스트 대상에 `@InjectMocks` 적용
- [ ] 의존성에 `@Mock` 적용
- [ ] `@BeforeEach`로 공통 Mock 데이터 초기화
- [ ] `@Nested`로 관련 테스트 그룹화
- [ ] Given-When-Then 패턴 준수
- [ ] `@DisplayName`으로 테스트 설명 작성
- [ ] `assertThat()`으로 결과 검증
- [ ] `verify()`로 메서드 호출 검증

---

## 14. Mock vs 실제 테스트 비교

| 구분 | Mock 테스트 (단위 테스트) | 통합 테스트 |
|------|--------------------------|-------------|
| DB 연결 | 불필요 | 필요 |
| 속도 | 빠름 (ms) | 느림 (초) |
| 격리성 | 높음 | 낮음 |
| 어노테이션 | `@ExtendWith(MockitoExtension.class)` | `@SpringBootTest` |
| 용도 | 비즈니스 로직 검증 | 전체 흐름 검증 |

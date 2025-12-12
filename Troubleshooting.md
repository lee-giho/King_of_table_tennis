# 🧩 Troubleshooting

## 🗂️ 목차
- [1. 상대를 찾지 못한 경기의 상태가 계속 `RECRUITING` / `WAITING`으로 남는 문제](#1-상대를-찾지-못한-경기의-상태가-계속-recruiting--waiting으로-남는-문제)
  - [🧱 문제 상황](#-문제-상황)
  - [🔍 원인 분석](#-원인-분석)
  - [🎯 해결 전략](#-해결-전략)
  - [🧩 구현 요약](#-구현-요약)
  - [🎉 개선 효과](#-개선-효과)
  - [📋 핵심 코드](#-핵심-코드)
- [2. 채팅 메시지 전송 후 채팅방 목록 프리뷰가 커밋 이전 데이터로 전송될 수 있는 문제](#2-채팅-메시지-전송-후-채팅방-목록-프리뷰가-커밋-이전-데이터로-전송될-수-있는-문제)
  - [🧱 문제 상황](#-문제-상황-1)
  - [🔍 원인 분석](#-원인-분석-1)
  - [🎯 해결 전략](#-해결-전략-1)
  - [🧩 구현 요약](#-구현-요약-1)
  - [🎉 개선 효과](#-개선-효과-1)
  - [📋 핵심 코드](#-핵심-코드-1)

## 1. 상대를 찾지 못한 경기의 상태가 계속 `RECRUITING` / `WAITING`으로 남는 문제

### 🧱 문제 상황

- 탁구 경기 매칭 기능에서 아래와 같은 이유로 *여전히 `RECRUITING` 또는 `WAITING` 상태로 남아 있는 문제**가 발생했다.
  - 상대를 찾지 못함
  - 경기 시간이 40분이 지나도 시작되지 않음
    
- 그 결과:
  - 사용자는 의미가 없는 “오래된 모집 경기”를 계속 보게 되고, 경기장별 경기 목록 UI가 실제 운영 상태와 맞지 않는 문제가 발생했다.

### 🔍 원인 분석

- 기존에는 경기 상태를 **명시적으로 바꿔줄 만료 처리 로직이 없었고**, 오직 사용자의 행동(참가, 종료)으로만 상태가 변경되었다.
- 별도 배치 작업이나 스케줄러가 없어서 계속 `RECRUITING` / `WAITING` 상태로 남아 있게 됐다.

### 🎯 해결 전략

1. **정상 종료와 구분되는 만료 상태 도입**
   - `GameState`에 `EXPIRED` 상태를 추가했다.
   - 의미:
     - `END` : 실제로 경기가 정상적으로 종료된 상태
     - `EXPIRED` : 상대를 찾지 못했거나, 경기 시간이 지났지만 시작되지 않아 시스템에 의해 만료된 상태

2. **경기 목록 조회 시점에 비동기 만료 처리**
   - `GameService.getRecruitingGameList()`가 호출될 때,
     - 해당 탁구장에 대해 **만료된 경기를 비동기로 `EXPIRED`로 변경**하는 작업을 트리거한다.
   - 이를 통해:
     - 트래픽이 있는 탁구장부터 자연스럽게 정리
     - 별도의 배치/스케줄러 없이도 점진적으로 상태 정리 가능

3. **쓰로틀링 + 경합 방지로 성능 & 동시성 고려**
   - 문제:  
     - 단순히 “조회할 때마다 만료 쿼리 실행”을 하면 사용자 수가 많을수록 같은 쿼리가 과도하게 반복 실행됨<br>
     → 동일한 업데이트 쿼리가 과도하게 실행되어 비효율
   - 해결:
     - **비동기 만료 작업의 마지막 실행 시각을 프로세스 전역에 저장**
     - 마지막 실행으로부터 **60초 이내에 다시 요청이 들어오면 만료 쿼리를 실행하지 않고 스킵** → *쓰로틀링*
     - 여러 요청이 동시에 들어와도 **최초 1개의 요청만 만료 작업을 수행**하도록 함 → *경합 방지*
     - 이 동시성 제어는 **락 없이 CAS(Compare And Set)** 으로 구현하여 비용을 최소화했다.

### 🧩 구현 요약
- 경기 상태에 `EXPIRED`를 추가하여 자동 만료를 명확히 표현
- 경기 목록 조회 시 비동기 만료 작업 트리거
- 만료 처리는 전용 스레드 풀에서 실행
- 마지막 실행 시각 기반 쓰로틀링 적용 (60초)
- CAS 기반 경합 방지로 중복 실행 차단
- 오래된 `RECRUITING` / `WAITING` 경기를 일괄 업데이트 쿼리로 처리

### 🎉 개선 효과
- 상대를 찾지 못한 오래된 경기들이 자동으로 `EXPIRED` 처리되어 사용자는 **실제로 참여 가능한 경기만 조회**할 수 있게 되었다.
- 경기 목록 UI와 실제 운영 상태 간 불일치 문제가 해소되었다.
- 쓰로틀링 + CAS 기반 제어를 통해 서버 및 DB 자원을 효율적으로 사용할 수 있게 되었다.
  - 높은 트래픽 상황에서도 불필요한 중복 업데이트를 방지
- 배치/스케줄러 없이도 **사용자 트래픽을 활용한 점진적 상태 정리 구조**를 구현할 수 있었다.
<br><br>

### 📋 핵심 코드
<details>
<summary>코드 보기</summary>
<div markdown="1">

#### 📌 1. 경기 목록 조회 시 "만료 처리"를 비동기로 트리거
```java
public PageResponse<RecruitingGameDTO> getRecruitingGameList(String tableTennisCourtId, String type, int page, int size) {
  
  ···

  asyncService.expireOutdatedGames(tableTennisCourtId); // ✅ 조회 시점에 비동기로 경기 만료 처리 트리거

  ···

  return new PageResponse<>(
    recruitingGamePage.getContent(),
    recruitingGamePage.getTotalPages(),
    recruitingGamePage.getTotalElements(),
    recruitingGamePage.getNumber(),
    recruitingGamePage.getSize()
  );
}
```
#### 📌 2. 비동기 만료 처리 (쓰로틀링 + CAS 기반 경합 방지)
```java
// ✅ 마지막 실행 시각을 프로세스 전역으로 관리
private final AtomicReference<Instant> lastRun = new AtomicReference<>(Instant.EPOCH);

@Async("expireExecutor")
@Transactional
public void expireOutdatedGames(String tableTennisCourtId) {
  Instant now = Instant.now(); // 시스템 기준 현재 시간
  Instant prev = lastRun.get(); // 마지막 실행 시각

  // 쓰로틀링 - 마지막 실행으로부터 60초 이내면 해당 호출 넘김
  if (prev.plusSeconds(60).isAfter(now)) {
    return;
  }
  // 경합 방지 - 여러 요청이 동시에 들어와도 최초 1개만 실행
  // prev가 lastRun의 현재 값일 때만 now로 교체 성공 -> 나머지 스킵
  if (!lastRun.compareAndSet(prev, now)) {
    return;
  }

  // 만료 기준 시각: 지금부터 40분 이전
  LocalDateTime cutoff = LocalDateTime.now().minusMinutes(40);

  // 상태가 RECRUITING/WAITING인데 경기 시작 시간이 만료 기준을 지난 경기를 EXPIRED로 업데이트
  gameStateRepository.expirePastRecruitingOrWaiting(tableTennisCourtId, cutoff);
}
```
#### 📌 3. 만료 대상 경기 일괄 업데이트 쿼리
```java
@Modifying(clearAutomatically = true, flushAutomatically = true)
@Query("""
  UPDATE GameStateEntity gs
  SET gs.state = com.giho.king_of_table_tennis.entity.GameState.EXPIRED
  WHERE gs.state IN (
      com.giho.king_of_table_tennis.entity.GameState.RECRUITING,
      com.giho.king_of_table_tennis.entity.GameState.WAITING
    )
    AND EXISTS (
      SELECT 1
      FROM GameInfoEntity gi
      WHERE gi.id = gs.gameInfoId
        AND gi.place = :placeId
        AND gi.gameDate <= :cutoff
    )
""")
int expirePastRecruitingOrWaiting(
  @Param("placeId") String placeId,
  @Param("cutoff")LocalDateTime cutoff
);
```
#### 📌 4. 비동기 작업 전용 스레드 풀 설정
```java
@Configuration
@EnableAsync
public class AsyncConfig {

  @Bean(name = "expireExecutor")
  public Executor expireExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(2); // 코어 스레드 수: 상시 유지되는 스레드 수(작없이 없을 때도 유지)
    executor.setMaxPoolSize(4); // 최대 스레드 수: 큐가 가득 차거나 바쁠 때 늘어날 수 있는 상한
    executor.setQueueCapacity(100); // 큐 용량: 코어 스레드가 바쁠 때 대기시킬 작업 수
    executor.setThreadNamePrefix("expire-"); // 스레드 이름 prefix
    executor.initialize();
    return executor;
  }
}
```

</div>
</details>

---

## 2. 채팅 메시지 전송 후 채팅방 목록 프리뷰가 커밋 이전 데이터로 전송될 수 있는 문제

### 🧱 문제 상황
- 사용자가 채팅 메시지를 전송하면 채팅방 목록 화면에 마지막 메시지, 마지막 전송 시각, 안 읽은 메시지 수가 실시간으로 반영되어야 했다.
- 초기 구현에서는 메시지 저장 및 채팅방 정보 업데이트 직후 **WebSocket으로 프리뷰 정보를 즉시 전송**하는 구조였다.
- 해당 로직이 `@Transactional` 범위 안에서 실행되면서 예외로 인해 트랜잭션이 롤백될 경우에도 이미 WebSocket으로는 “갱신된 정보”가 전송될 수 있는 문제가 존재했다.
- 이로 인해 **DB 상태와 클라이언트 UI 간 불일치 가능성**이 있었다.

### 🔍 원인 분석
- WebSocket 전송 시점이 트랜잭션 커밋 이전이라 DB에 실제로 반영되지 않은 데이터를 기준으로 갱신된 정보가 전송될 수 있었다.
- 데이터 변경 로직과 실시간 전파 로직이 하나의 흐름에 섞여 있어 트랜잭션 경계를 명확히 분리하지 못한 구조였다.

### 🎯 해결 전략
- 서비스 메서드에서는 **메시지 저장 및 채팅방 상태 변경까지만 수행**하고 프리뷰 갱신에 필요한 이벤트(`PreChatRoomUpdatedEvent`)만 발행하도록 수정했다.
- 실제 WebSocket 전송은 `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`에서 처리하도록 분리했다.
- **AFTER_COMMIT 이후에 Projection을 다시 조회(`findPreChatRoom`)하도록 하여 전송 데이터가 DB에 반영된 최종 상태임을 보장했다.**

### 🧩 구현 요약
- 메시지 저장 및 채팅방 업데이트 → 트랜잭션 내에서 처리
- 프리뷰 갱신 이벤트 발행 → 커밋 이전
- WebSocket 전송 →
  - 트랜잭션 커밋 이후(AFTER_COMMIT)
  - Projection 기반 조회(`findPreChatRoom`)
  - 안 읽은 메시지 수 재계산 후 전송

### 🎉 개선 효과
- 트랜잭션이 롤백되면 이벤트 리스너가 실행되지 않아 **커밋되지 않은 데이터가 클라이언트로 전송되는 문제를 방지**할 수 있었다.
- 커밋 이후 Projection을 다시 조회하는 구조로 채팅방 목록에 표시되는 정보가 **항상 DB에 반영된 최종 상태와 일치**하도록 보장했다.
- WebSocket을 활용한 실시간 갱신은 유지하면서도 데이터의 정합성과 일관성을 함께 확보한 구조가 되었다.
<br><br>

### 📋 핵심 코드
<details>
<summary>코드 보기</summary>
<div markdown="1">

#### 📌 1. 메시지 저장 후 이벤트 발행 (트랜잭션 내부)
```java
private final ApplicationEventPublisher eventPublisher;

@Transactional
public ChatMessage sendMessage(String token, SendMessagePayload sendMessagePayload) {

  ... // 메시지 저장 및 업데이트

  // ✅ 이벤트만 발행하고 WebSocket 전송 X
  eventPublisher.publishEvent(
    new PreChatRoomUpdatedEvent(
      chatRoomEntity.getId(),
      chatRoomEntity.getCreatorId(),
      chatRoomEntity.getParticipantId()
    )
  );

  return ChatMessage.builder()
    .id(savedChatMessageEntity.getId())
    .roomId(savedChatMessageEntity.getRoomId())
    .senderId(savedChatMessageEntity.getSenderId())
    .content(savedChatMessageEntity.getContent())
    .sentAt(savedChatMessageEntity.getSentAt())
    .build();
}
```
#### 📌 2. 커밋 이후(AFTER_COMMIT) Projection 재조회 + WebSocket 전송
```java
@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePreChatRoomUpdated(PreChatRoomUpdatedEvent event) {
    String roomId = event.roomId();
    String creatorId = event.creatorId();
    String participantId = event.participantId();

    // ✅ 커밋 이후 최종 상태를 Projection으로 재조회
    PreChatRoom forCreator = chatRoomRepository.findPreChatRoom(roomId, creatorId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    PreChatRoom forParticipant = chatRoomRepository.findPreChatRoom(roomId, participantId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    List<String> singleRoomId = List.of(roomId);

    // creator 기준
    long creatorUnread = chatMessageRepository.countUnreadMessagesByRoomId(singleRoomId, creatorId)
      .stream()
      .findFirst()
      .map(RoomUnreadCount::getUnreadCount)
      .orElse(0L);

    // participant 기준
    long participantUnread = chatMessageRepository.countUnreadMessagesByRoomId(singleRoomId, participantId)
      .stream()
      .findFirst()
      .map(RoomUnreadCount::getUnreadCount)
      .orElse(0L);

    forCreator.setUnreadCount((int) creatorUnread);
    forParticipant.setUnreadCount((int) participantUnread);

    // ✅ 커밋 이후에만 WebSocket으로 전송
    webSocketMessageSender.sendPreChatRoom(
      forCreator,
      forParticipant,
      creatorId,
      participantId
    );
  }
```
#### 📌 3. 이벤트 Payload 간소화
```java
public record PreChatRoomUpdatedEvent(
  String roomId,
  String creatorId,
  String participantId
) {}
```
</div>
</details>

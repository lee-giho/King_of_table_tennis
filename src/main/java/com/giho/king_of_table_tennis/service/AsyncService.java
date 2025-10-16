package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncService {

  private final GameStateRepository gameStateRepository;

  // 비동기 만료 작업의 마지막 실행 시각을 프로세스 전역으로 저장
  // 다중 스레드에서 60초 쓰로틀링을 lock 없이 CAS(Compare And Set)으로 구현
  private final AtomicReference<Instant> lastRun = new AtomicReference<>(Instant.EPOCH);

  @Async("expireExecutor")
  @Transactional
  public void expireOutdatedGames(String tableTennisCourtId) {
    Instant now = Instant.now(); // 시스템 기준 현재 시간
    Instant prev = lastRun.get(); // 마지막 실행 시각

    // 쓰로틀링 - 마지막 실행으로부터 60초 이내면 해당 호출 넘김
    if (prev.plusSeconds(60).isAfter(now)) {
      log.debug("경기 만료 처리 넘김: lastRun={}, now={}", prev, now);
      return;
    }
    // 경합 방지 - 여러 요청이 동시에 들어와도 최초 1개만 실행
    // prev가 lastRun의 현재 값일 때만 now로 교체 성공 -> 나머지 스킵
    if (!lastRun.compareAndSet(prev, now)) {
      log.debug("경기 만료 처리 넘김: 다른 스레드에서 업데이트 됨");
      return;
    }

    try {
      // 만료 기준 시각: 지금부터 40분 이전
      LocalDateTime cutoff = LocalDateTime.now().minusMinutes(40);

      // 상태가 RECRUITING/WAITING인데 경기 시작 시간이 만료 기준을 지난 경기를 EXPIRED로 업데이트
      int updated = gameStateRepository.expirePastRecruitingOrWaiting(tableTennisCourtId, cutoff);
      System.out.println(cutoff + "보다 오래된 게임이 만료되었습니다. -> " + updated + "개 만료");
      log.debug("{}보다 오래된 게임이 만료되었습니다. -> {}개 만료", cutoff, updated);
    } catch (Exception e) {
      // 비동기 작업 실패 시 로그만 남김
      log.error("경기 만료 실패", e);
    }
  }
}

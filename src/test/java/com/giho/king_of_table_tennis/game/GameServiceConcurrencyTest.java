package com.giho.king_of_table_tennis.game;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.GameParticipationRequestDTO;
import com.giho.king_of_table_tennis.entity.AcceptanceType;
import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import com.giho.king_of_table_tennis.entity.GameState;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import com.giho.king_of_table_tennis.repository.GameInfoRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import com.giho.king_of_table_tennis.service.GameService;
import com.giho.king_of_table_tennis.util.EnvLoader;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GameServiceConcurrencyTest {

  @Container
  static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
    .withDatabaseName("test-db")
    .withUsername("test")
    .withPassword("test");

  @DynamicPropertySource
  static void setDatasourceProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
  }

  @Autowired
  private GameService gameService;

  @Autowired
  private GameInfoRepository gameInfoRepository;

  @Autowired
  private GameStateRepository gameStateRepository;

  @BeforeAll
  static void initEnv() {
    EnvLoader.loadEnv();
  }

  @BeforeEach
  void setUp() {
    // 선착순 방식 게임 정보 및 상태 등록
    GameInfoEntity gameInfo = new GameInfoEntity();
    gameInfo.setId("test-game-id");
    gameInfo.setGameSet(3);
    gameInfo.setGameScore(11);
    gameInfo.setPlace("687fcc0773c25268cad7d768");
    gameInfo.setAcceptanceType(AcceptanceType.FCFS);
    gameInfo.setGameDate(LocalDateTime.now());
    gameInfoRepository.save(gameInfo);

    GameStateEntity gameState = new GameStateEntity();
    gameState.setDefenderId("defender-id");
    gameState.setGameInfoId(gameInfo.getId());
    gameState.setState(GameState.RECRUITING);
    gameStateRepository.save(gameState);

    GameInfoEntity savedGameInfo = gameInfoRepository.findById("test-game-id")
      .orElseThrow();
    System.out.println("savedGameInfo: " + savedGameInfo.getId());

    GameStateEntity savedGameState = gameStateRepository.findByGameInfoId("test-game-id")
      .orElseThrow();
    System.out.println("savedGameState: " + savedGameState.getGameInfoId());
  }

  @Test
  @DisplayName("동시에 여러 명이 게임 참가 신청을 하는 테스트 (DB락 테스트)")
  void concurrentGameParticipationTest() throws InterruptedException {

    // Given: 쓰레드 10개, CountDownLatch 준비
    int threadCount = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
    CountDownLatch latch = new CountDownLatch(threadCount);

    List<Future<BooleanResponseDTO>> results = new ArrayList<>();

    // When: gameParticipation 동시에 요청
    for (int i = 0; i < threadCount; i++) {
      final int userId = i + 1;
      results.add(executorService.submit(() -> {
        try {
          GameParticipationRequestDTO dto = new GameParticipationRequestDTO();
          dto.setGameInfoId("test-game-id");
          dto.setChallengerId("user-" + userId);
          return gameService.gameParticipation(dto);
        } catch (Exception e) {
          System.out.println("참여 실패: " + e.getMessage());
          return new BooleanResponseDTO(false);
        } finally {
          latch.countDown();
        }
      }));
    }

    latch.await();

    // Then: 성공한 참가자는 1명이어야 함
    long successCount = results.stream()
      .filter(future -> {
        try {
          return future.get().isSuccess();
        } catch (Exception e) {
          return false;
        }
      }).count();

    System.out.println("성공한 참여 수: " + successCount);
    assertThat(successCount).isEqualTo(1); // 선착순이므로 1명만 성공
  }
}
package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameStateEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface GameStateRepository extends JpaRepository<GameStateEntity, String> {

  Optional<GameStateEntity> findByGameInfoId(String gameInfoId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<GameStateEntity> findWithLockByGameInfoId(String gameInfoId);

  List<GameStateEntity> findAllByGameInfoIdIn(List<String> gameInfoIds);

  void deleteByGameInfoId(String gameInfoId);

  // 경기를 하지 못하고 일정 시간 지난 경기의 상태를 EXPIRED로 바꾸는 쿼리
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
  int expirePastRecruitingOrWaiting(@Param("placeId") String placeId, @Param("cutoff")LocalDateTime cutoff);
}

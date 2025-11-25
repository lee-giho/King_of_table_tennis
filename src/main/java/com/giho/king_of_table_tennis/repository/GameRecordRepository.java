package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.GameRecordRow;
import com.giho.king_of_table_tennis.dto.GameStatsProjection;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameRecordRepository extends JpaRepository<GameStateEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.GameRecordRow(
      gi.id,
      
      me.id,
      me.nickName,
      me.profileImage,
      ttiMe.racketType,
      CASE
        WHEN (gs.defenderId = :userId AND gs.defenderScore > gs.challengerScore)
          OR (gs.challengerId = :userId AND gs.challengerScore > gs.defenderScore)
        THEN gi.gameSet
        ELSE 0
      END,
      
      opp.id,
      opp.nickName,
      opp.profileImage,
      ttiOpp.racketType,
      CASE
        WHEN (gs.defenderId = : userId AND gs.defenderScore < gs.challengerScore)
          OR (gs.challengerId = : userId AND gs.challengerScore < gs.defenderScore)
        THEN gi.gameSet
        ELSE 0
      END,
      
      gi.place,
      gi.gameDate
    )
    FROM GameStateEntity gs
      JOIN GameInfoEntity gi ON gi.id = gs.gameInfoId
      JOIN UserEntity me ON me.id = :userId
      LEFT JOIN UserTableTennisInfoEntity ttiMe ON ttiMe.userId = me.id
      JOIN UserEntity opp
        ON (
          (gs.defenderId = :userId AND opp.id = gs.challengerId)
          OR
          (gs.challengerId = :userId AND opp.id = gs.defenderId)
        )
      LEFT JOIN UserTableTennisInfoEntity ttiOpp ON ttiOpp.userId = opp.id
    WHERE (gs.defenderId = :userId OR gs.challengerId = :userId)
      AND gs.state = 'END'
    ORDER BY gi.gameDate DESC
  """)
  Page<GameRecordRow> findGameRecordsByUser(
    @Param("userId") String userId,
    Pageable pageable
  );

  @Query("""
    SELECT
      r.totalGames AS totalGames,
      r.winCount AS winCount,
      r.defeatCount AS defeatCount,
      r.winRate AS winRate
    FROM UserRankingEntity r
    WHERE r.userId = :userId
  """)
  Optional<GameStatsProjection> findTotalStats(
    @Param("userId") String userId
  );

  @Query(value = """
    SELECT
      COUNT(*) AS totalGames,
      
      SUM(
        CASE
          WHEN (
              (gs.defender_id = :userId AND gs.defender_score > gs.challenger_score)
              OR
              (gs.challenger_id = :userId AND gs.challenger_score > gs.defender_score)
          ) THEN 1 ELSE 0
        END
      ) AS winCount,
        
      SUM(
        CASE
          WHEN (
              (gs.defender_id = :userId AND gs.defender_score < gs.challenger_score)
              OR
              (gs.challenger_id = :userId AND gs.challenger_score < gs.defender_score)
          ) THEN 1 ELSE 0
        END
      ) AS defeatCount,
      
      CASE
        WHEN COUNT(*) = 0 THEN 0
        ELSE SUM(
          CASE
            WHEN (
              (gs.defender_id = :userId AND gs.defender_score > gs.challenger_score)
              OR
              (gs.challenger_id = :userId AND gs.challenger_score > gs.defender_score)
            ) THEN 1 ELSE 0
          END
        ) / COUNT(*)
      END AS winRate
      
    FROM (
      SELECT gs.*
      FROM game_state gs
        JOIN game_info gi ON gi.id = gs.game_info_id
      WHERE gs.state = 'END'
        AND (gs.defender_id = :userId OR gs.challenger_id = :userId)
      ORDER BY gi.game_date DESC
      LIMIT 10
    ) AS gs
  """, nativeQuery = true)
  GameStatsProjection findRecentStats(
    @Param("userId") String userId
  );
}

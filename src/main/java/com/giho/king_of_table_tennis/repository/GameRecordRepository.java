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
        WHEN gs.defenderId = :userId THEN gr.defenderSetScore
        ELSE gr.challengerSetScore
      END,
      
      opp.id,
      opp.nickName,
      opp.profileImage,
      ttiOpp.racketType,
      CASE
        WHEN gs.defenderId = :userId THEN gr.challengerSetScore
        ELSE gr.defenderSetScore
      END,
      
      gi.place,
      gi.gameDate
    )
    FROM GameStateEntity gs
      JOIN GameInfoEntity gi ON gi.id = gs.gameInfoId
      JOIN GameResultEntity gr ON gr.gameInfoId = gi.id
      
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
      
      COALESCE(SUM(
        CASE
          WHEN gr.winner_id = :userId THEN 1
          ELSE 0
        END
      ), 0) AS winCount,
      
      COALESCE(SUM(
        CASE
          WHEN gr.loser_id = :userId THEN 1
          ELSE 0
        END
      ), 0) AS defeatCount,
      
      CASE
        WHEN COUNT(*) = 0 THEN 0
        ELSE COALESCE(SUM(
          CASE
            WHEN gr.winner_id = :userId THEN 1
            ELSE 0
          END
        ), 0) * 1.0 / COUNT(*)
      END AS winRate
      
    FROM (
        SELECT gs.game_info_id
        FROM game_state gs
          JOIN game_info gi ON gi.id = gs.game_info_id
        WHERE gs.state = 'END'
          AND (gs.defender_id = :userId OR gs.challenger_id = :userId)
        ORDER BY gi.game_date DESC
        LIMIT 10
      ) AS recent
      JOIN game_result gr ON gr.game_info_id = recent.game_info_id
  """, nativeQuery = true)
  GameStatsProjection findRecentStats(
    @Param("userId") String userId
  );
}

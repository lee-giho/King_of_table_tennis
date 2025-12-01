package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.GameDetailInfo;
import com.giho.king_of_table_tennis.dto.GameDetailInfoByPage;
import com.giho.king_of_table_tennis.dto.RecruitingGameDTO;
import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import com.giho.king_of_table_tennis.entity.GameState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GameInfoRepository extends JpaRepository<GameInfoEntity, String> {

  List<GameInfoEntity> findAllByPlaceAndGameDateAfter(String place, LocalDateTime now);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.RecruitingGameDTO(
      g,
      s.defenderId,
      s.state,
      CASE WHEN s.defenderId = :userId THEN true ELSE false END,
      CASE WHEN EXISTS (
        SELECT 1
        FROM GameApplicationEntity ga
        WHERE ga.gameInfoId = g.id
          AND ga.applicantId = :userId
      ) THEN true ELSE false END
    )
    FROM GameInfoEntity g
      JOIN GameStateEntity s ON g.id = s.gameInfoId
    WHERE g.place = :place
      AND s.state IN :gameStates
  """)
  Page<RecruitingGameDTO> findGamesByPlaceAndState(
    @Param("place") String place,
    @Param("gameStates") List<GameState> gameStates,
    @Param("userId") String userId,
    Pageable pageable
  );


  @Query("""
    SELECT gi
    FROM GameInfoEntity gi
      JOIN GameStateEntity gs ON gs.gameInfoId = gi.id
    WHERE gi.place = :place
      AND gs.state IN :gameStates
  """)
  Page<GameInfoEntity> findGameInfoEntityByPlaceAndStates(
    @Param("place") String place,
    @Param("gameStates") List<GameState> gameStates,
    Pageable pageable
  );

  @Query("""
    SELECT g, s, d, c, dTti, cTti, dFriend, cFriend, dRanking, cRanking,
      ( SELECT COUNT(ga)
        FROM GameApplicationEntity ga
        WHERE ga.gameInfoId = g.id
      ) AS applicationCount,
      ( CASE WHEN EXISTS (
          SELECT 1
          FROM GameReviewEntity gr
          WHERE gr.gameInfoId = g.id
            AND gr.reviewerId = :userId
        ) THEN true ELSE false END
      ) AS hasReviewed
    FROM GameInfoEntity g
      JOIN GameStateEntity s ON g.id = s.gameInfoId
      JOIN UserEntity d ON d.id = s.defenderId
      LEFT JOIN UserEntity c ON c.id = s.challengerId
      LEFT JOIN UserTableTennisInfoEntity dTti ON dTti.userId = d.id
      LEFT JOIN UserTableTennisInfoEntity cTti ON cTti.userId = c.id
      LEFT JOIN FriendEntity dFriend ON dFriend.userId = :userId AND dFriend.friendId = d.id
      LEFT JOIN FriendEntity cFriend ON cFriend.userId = :userId AND cFriend.friendId = c.id
      LEFT JOIN UserRankingEntity dRanking ON dRanking.userId = d.id
      LEFT JOIN UserRankingEntity cRanking ON cRanking.userId = c.id
    WHERE (s.defenderId = :userId OR s.challengerId = :userId)
      AND s.state IN :gameStates
    ORDER BY g.gameDate ASC
  """)
  Page<Object[]> findByUserIdAndGameState(@Param("userId") String userId, @Param("gameStates") List<GameState> gameStates, Pageable pageable);
}

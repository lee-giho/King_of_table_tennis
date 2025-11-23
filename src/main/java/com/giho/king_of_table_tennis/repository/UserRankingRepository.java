package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.UserRankingInfo;
import com.giho.king_of_table_tennis.entity.UserRankingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRankingRepository extends JpaRepository<UserRankingEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserRankingInfo(
      u.id, u.nickName, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, 0,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END
    )
    FROM UserRankingEntity ranking
      JOIN UserEntity u ON u.id = ranking.userId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
      LEFT JOIN UserBlockEntity ub ON ub.blockerId = :currentUserId AND ub.blockedId = u.id
  """)
  Page<UserRankingInfo> findUserRankingInfos(
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserRankingInfo(
      u.id, u.nickName, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, 0,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END
    )
    FROM UserRankingEntity ranking
      JOIN UserEntity u ON u.id = ranking.userId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
      LEFT JOIN UserBlockEntity ub ON ub.blockerId = :currentUserId AND ub.blockedId = u.id
    WHERE u.id = :userId
  """)
  Optional<UserRankingInfo> findUserRankingInfoByUserId(
    @Param("currentUserId") String currentUserId,
    @Param("userId") String userId
  );

  @Query(value = """
    SELECT t.rnk
    FROM (
      SELECT
        u.id AS id,
        RANK() OVER (
          ORDER BY
            CASE WHEN :sort = 'WIN_RATE' THEN r.win_rate END DESC,
            CASE WHEN :sort = 'WIN_RATE' THEN r.win_count END DESC,
            CASE WHEN :sort = 'WIN_COUNT' THEN r.win_count END DESC,
            CASE WHEN :sort = 'WIN_COUNT' THEN r.win_rate END DESC
        ) AS rnk
      FROM user_ranking r
        JOIN user u ON u.id = r.user_id
    ) AS t
    WHERE t.id = :userId
  """, nativeQuery = true)
  Integer findUserRankBySort(
    @Param("userId") String UserId,
    @Param("sort") String sort
  );
}

package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.GameApplicationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GameApplicationRepository extends JpaRepository<GameApplicationEntity, String> {
  Optional<GameApplicationEntity> findByGameInfoIdAndApplicantId(String gameInfoId, String applicantId);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status
    )
    FROM GameApplicationEntity ga
      JOIN UserEntity u ON u.id = ga.applicantId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
      LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE ga.gameInfoId = :gameInfoId
    ORDER BY ga.applicationAt ASC
  """)
  Page<UserInfo> findApplicantByGameInfoIdOrderByApplicationAtAsc(
    @Param("gameInfoId") String gameInfoId,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );

  boolean existsByGameInfoIdAndApplicantId(String gameInfoId, String applicantId);

  void deleteByGameInfoId(String gameInfoId);
}

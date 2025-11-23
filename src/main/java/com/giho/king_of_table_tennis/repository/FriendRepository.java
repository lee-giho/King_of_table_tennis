package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.CountResponseDTO;
import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.FriendEntity;
import com.giho.king_of_table_tennis.entity.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status
    )
    FROM FriendEntity f
      JOIN UserEntity u ON u.id = f.friendId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
    WHERE f.userId = :userId
      AND f.status = com.giho.king_of_table_tennis.entity.FriendStatus.RECEIVED
  """)
  Page<UserInfo> findReceivedFriendRequests(
    @Param("userId") String userId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.CountResponseDTO(
      COUNT(f)
    )
    FROM FriendEntity f
    WHERE f.userId = :userId
      AND f.status = :friendStatus
  """)
  CountResponseDTO countReceivedFriendRequests(
    @Param("userId") String userId,
    @Param("friendStatus") FriendStatus friendStatus
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.CountResponseDTO(
      COUNT(f)
    )
    FROM FriendEntity f
    WHERE f.userId = :userId
      AND f.status = com.giho.king_of_table_tennis.entity.FriendStatus.FRIEND
      AND EXISTS (
        SELECT 1
        FROM UserBlockEntity ub
        WHERE ub.blockerId = :userId
          AND ub.blockedId = f.friendId
      )
  """)
  CountResponseDTO countBlockedFriend(
    @Param("userId") String userId
  );

  Optional<FriendEntity> findFriendEntityByUserIdAndFriendId(String userId, String friendId);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status
    )
    FROM FriendEntity f
      JOIN UserEntity u ON u.id = f.friendId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
    WHERE f.userId = :userId
      AND f.status = com.giho.king_of_table_tennis.entity.FriendStatus.FRIEND
      AND NOT EXISTS (
        SELECT 1
        FROM UserBlockEntity ub1
        WHERE ub1.blockerId = :userId
          AND ub1.blockedId = u.id
      )
      AND NOT EXISTS (
        SELECT 1
        FROM UserBlockEntity ub2
        WHERE ub2.blockerId = u.id
          AND ub2.blockedId = :userId
      )
    ORDER BY u.nickName ASC
  """)
  Page<UserInfo> findMyFriends(
    @Param("userId") String userId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status
    )
    FROM FriendEntity f
      JOIN UserEntity u ON u.id = f.friendId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
    WHERE f.userId = :userId
      AND EXISTS (
        SELECT 1
        FROM UserBlockEntity ub
        WHERE ub.blockerId = :userId
          AND ub.blockedId = u.id
      )
    ORDER BY u.nickName ASC
  """)
  Page<UserInfo> findMyBlockedFriends(
    @Param("userId") String userId,
    Pageable pageable
  );

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("""
    DELETE FROM FriendEntity f
    WHERE (f.userId = :userId AND f.friendId = :targetUserId)
      OR (f.userId = :targetUserId AND f.friendId = :userId)
  """)
  void deleteBothRelations(
    @Param("userId") String userId,
    @Param("targetUserId") String targetUserId
  );

  @Query("""
    SELECT COUNT(f) > 0
    FROM FriendEntity  f
    WHERE (
        (f.userId = :userId AND f.friendId = :targetUserId)
        OR
        (f.userId = :targetUserId AND f.friendId = :userId)
      )
      AND f.status = com.giho.king_of_table_tennis.entity.FriendStatus.FRIEND
  """)
  boolean existsMutualFriendRelation(
    @Param("userId") String userId,
    @Param("targetUserId") String targetUserId
  );
}

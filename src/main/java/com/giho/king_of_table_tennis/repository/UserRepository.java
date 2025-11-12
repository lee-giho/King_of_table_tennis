package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.GameUserInfo;
import com.giho.king_of_table_tennis.dto.MySimpleInfoResponse;
import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsById(String id);

  boolean existsByNickName(String nickName);

  Optional<UserEntity> findByNameAndEmail(String name, String email);

  Optional<UserEntity> findByIdAndNameAndEmail(String id, String name, String email);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END
    )
    FROM UserEntity u
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    LEFT JOIN UserBlockEntity ub ON ub.blockerId = :currentUserId AND ub.blockedId = u.id
    WHERE u.id IN :userIds
  """)
  List<UserInfo> findUserInfoByIds(@Param("userIds") List<String> userIds, @Param("currentUserId") String currentUserId);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END
    )
    FROM UserEntity u
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    LEFT JOIN UserBlockEntity ub ON ub.blockerId = :currentUserId AND ub.blockedId = u.id
    WHERE u.id = :id
  """)
  Optional<UserInfo> findUserInfoById(@Param("id") String id, @Param("currentUserId") String currentUserId);

  List<UserEntity> findByIdIn(List<String> userIds);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.GameUserInfo(
    u.id, u.nickName, u.profileImage,
    tti.racketType
    )
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id IN :userIds
  """)
  List<GameUserInfo> findGameUserInfoByIds(@Param("userIds") List<String> userIds);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.MySimpleInfoResponse(
      u.nickName, u.profileImage,
      tti.racketType, tti.winCount, tti.defeatCount
    )
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id = :id
  """)
  MySimpleInfoResponse findMySimpleInfoById(@Param("id") String id);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END
    )
    FROM UserEntity u
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    LEFT JOIN UserBlockEntity ub ON ub.blockerId = :currentUserId AND ub.blockedId = u.id
    WHERE u.id <> :currentUserId
      AND (
        :keyword IS NULL
        OR :keyword = ''
        OR LOWER(u.nickName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
      AND (:onlyFriend = false OR f.status = com.giho.king_of_table_tennis.entity.FriendStatus.FRIEND)
      AND NOT EXISTS (
        SELECT 1
        FROM UserBlockEntity ub2
        WHERE ub2.blockerId = u.id
          AND ub2.blockedId = :currentUserId
      )
    ORDER BY u.nickName ASC
  """)
  Page<UserInfo> searchUserInfoByKeyword(
    @Param("keyword") String keyword,
    @Param("onlyFriend") boolean onlyFriend,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );
}

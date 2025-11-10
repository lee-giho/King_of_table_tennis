package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.CountResponseDTO;
import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.FriendEntity;
import com.giho.king_of_table_tennis.entity.FriendStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<FriendEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      f.status
    )
    FROM FriendEntity f
    JOIN UserEntity u ON u.id = f.friendId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
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

  Optional<FriendEntity> findFriendEntityByUserIdAndFriendId(String userId, String friendId);
}

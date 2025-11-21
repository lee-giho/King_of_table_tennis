package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.PreChatRoom;
import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

  Optional<ChatRoomEntity> findByCreatorIdAndParticipantIdOrCreatorIdAndParticipantId(
    String creatorId1, String participantId1,
    String creatorId2, String participantId2
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.PreChatRoom(
      cr.id,
      friend.id,
      friend.name, friend.nickName, friend.email, friend.profileImage,
      Tti.racketType, Tti.userLevel, Tti.winCount, Tti.defeatCount,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END,
      cr.createdAt,
      cr.lastMessage,
      cr.lastSentAt
    )
    FROM ChatRoomEntity cr
      JOIN UserEntity friend
        ON (
          (cr.creatorId = :userId AND friend.id = cr.participantId)
          OR
          (cr.participantId = :userId AND friend.id = cr.creatorId)
        )
      LEFT JOIN UserTableTennisInfoEntity Tti ON Tti.userId = friend.id
      LEFT JOIN FriendEntity f ON f.userId = :userId
        AND f.friendId = friend.id
      LEFT JOIN UserBlockEntity ub ON ub.blockerId = :userId
        AND ub.blockedId = friend.id
      LEFT JOIN ChatRoomUserStateEntity state ON state.roomId = cr.id
        AND state.userId = :userId
    WHERE (cr.creatorId = :userId OR cr.participantId = :userId)
      AND (state IS NULL OR state.deleted = false)
    ORDER BY COALESCE(cr.lastSentAt, cr.createdAt) DESC
  """)
  Page<PreChatRoom> findMyPreChatRooms(
    @Param("userId") String userId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.PreChatRoom(
      cr.id,
      friend.id,
      friend.name, friend.nickName, friend.email, friend.profileImage,
      Tti.racketType, Tti.userLevel, Tti.winCount, Tti.defeatCount,
      CASE
        WHEN ub.blockerId IS NOT NULL THEN com.giho.king_of_table_tennis.entity.FriendStatus.BLOCKED
        WHEN f.status IS NOT NULL THEN f.status
        ELSE com.giho.king_of_table_tennis.entity.FriendStatus.NOTHING
      END,
      cr.createdAt,
      cr.lastMessage,
      cr.lastSentAt
    )
    FROM ChatRoomEntity cr
      JOIN UserEntity friend
        ON (
          (cr.creatorId = :userId AND friend.id = cr.participantId)
          OR
          (cr.participantId = :userId AND friend.id = cr.creatorId)
        )
      LEFT JOIN UserTableTennisInfoEntity Tti ON Tti.userId = friend.id
      LEFT JOIN FriendEntity f ON f.userId = :userId
        AND f.friendId = friend.id
      LEFT JOIN UserBlockEntity ub ON ub.blockerId = :userId
        AND ub.blockedId = friend.id
        LEFT JOIN ChatRoomUserStateEntity state ON state.roomId = cr.id
        AND state.userId = :userId
    WHERE cr.id = :roomId
      AND (cr.creatorId = :userId OR cr.participantId = :userId)
      AND (state IS NULL OR state.deleted = false)
  """)
  Optional<PreChatRoom> findPreChatRoom(
    @Param("roomId") String roomId,
    @Param("userId") String userId
  );
}

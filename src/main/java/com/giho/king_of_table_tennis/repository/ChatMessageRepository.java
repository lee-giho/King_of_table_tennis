package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.dto.RoomUnreadCount;
import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

  Page<ChatMessageEntity> findByRoomId(
    @Param("roomId") String roomId,
    Pageable pageable
  );

  Optional<ChatMessageEntity> findTopByRoomIdOrderByIdDesc(String roomId);

  @Query(value = """
    SELECT
      m.room_id AS roomId,
      COUNT(*) AS unreadCount
    FROM chat_message m
    LEFT JOIN chat_read_state rs ON rs.room_id = m.room_id
      AND rs.user_id = :userId
    WHERE m.room_id IN (:roomIds)
      AND m.sender_id <> :userId
      AND (rs.last_read_message_id IS NULL OR m.id > rs.last_read_message_id)
    GROUP BY m.room_id
  """, nativeQuery = true)
  List<RoomUnreadCount> countUnreadMessagesByRoomId(
    @Param("roomIds") List<String> roomIds,
    @Param("userId") String userId
  );
}

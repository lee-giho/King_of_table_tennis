package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.ChatMessage(
      m.id, m.roomId, m.senderId, m.content, m.sentAt
    )
    FROM ChatMessageEntity m
    WHERE m.roomId = :roomId
  """)
  Page<ChatMessage> findChatMessagesByRoomId(
    @Param("roomId") String roomId,
    Pageable pageable
  );

  long countByRoomIdAndSenderIdNotAndIdGreaterThan(String roomId, String senderId, Long lastReadMessageId);
}

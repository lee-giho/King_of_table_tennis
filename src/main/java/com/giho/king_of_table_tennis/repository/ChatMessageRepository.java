package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

  Page<ChatMessageEntity> findByRoomId(
    @Param("roomId") String roomId,
    Pageable pageable
  );

  Optional<ChatMessageEntity> findTopByRoomIdOrderByIdDesc(String roomId);

  long countByRoomIdAndSenderIdNotAndIdGreaterThan(String roomId, String senderId, Long lastReadMessageId);
}

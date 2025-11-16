package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

  Page<ChatMessageEntity> findByRoomIdOrderByIdDesc(String roomId, Pageable pageable);

  long countByRoomIdAndSenderIdNotAndIdGreaterThan(String roomId, String senderId, Long lastReadMessageId);
}

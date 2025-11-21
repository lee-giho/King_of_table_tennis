package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.ChatReadStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatReadStateRepository extends JpaRepository<ChatReadStateEntity, Long> {

  Optional<ChatReadStateEntity> findByRoomIdAndUserId(String roomId, String userId);

  boolean existsByRoomIdAndUserId(String roomId, String userId);
}

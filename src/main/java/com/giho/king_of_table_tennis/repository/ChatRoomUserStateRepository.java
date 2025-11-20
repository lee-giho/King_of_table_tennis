package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.ChatRoomUserStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomUserStateRepository extends JpaRepository<ChatRoomUserStateEntity, Long> {

  Optional<ChatRoomUserStateEntity> findByRoomIdAndUserId(String roomId, String userId);
}

package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, String> {

  Optional<ChatRoomEntity> findByCreatorIdAndParticipantIdOrCreatorIdAndParticipantId(
    String creatorId1, String participantId1,
    String creatorId2, String participantId2
  );
}

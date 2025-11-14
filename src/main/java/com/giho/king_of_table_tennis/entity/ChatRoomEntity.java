package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
  name = "chat_room",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uq_chat_pair",
      columnNames = {"creator_id", "participant_id"}
    )
  }
)
public class ChatRoomEntity {
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "creator_id", nullable = false)
  private String creatorId;

  @Column(name = "participant_id", nullable = false)
  private String participantId;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "last_message")
  private String lastMessage;

  @Column(name = "last_sent_at")
  private String lastSentAt;
}

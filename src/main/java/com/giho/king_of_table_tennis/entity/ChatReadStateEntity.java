package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(
  name = "chat_read_state",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "up_chat_read_unique",
      columnNames = {"room_id", "user_id"}
    )
  }
)
public class ChatReadStateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "room_id", nullable = false)
  private String roomId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "last_read_message_id", nullable = true)
  private Long lastReadMessageId;

  @Column(name = "last_read_at", nullable = true)
  private LocalDateTime lastReadAt;

  public void updateReadState(Long messageId, LocalDateTime readAt) {
    // 더 최신 메시지일 때
    if (this.lastReadMessageId == null || messageId > this.lastReadMessageId) {
      this.lastReadMessageId = messageId;
      this.lastReadAt = readAt;
    }
  }
}

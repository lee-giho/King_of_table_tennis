package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chat_message")
public class ChatMessageEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "room_id", nullable = false)
  private String roomId;

  @Column(name = "sender_id", nullable = false)
  private String senderId;

  @Column(name = "content", nullable = false)
  private String content;

  @CreationTimestamp
  @Column(name = "sent_at", updatable = false, insertable = false)
  private LocalDateTime sentAt;

  public boolean isSentBy(String userId) {
    return this.senderId.equals(userId);
  }
}

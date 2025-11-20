package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(
  name = "chat_room_user_state",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uq_room_user",
      columnNames = {"room_id", "user_id"}
    )
  }
)
public class ChatRoomUserStateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "room_id", nullable = false)
  private String roomId;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "deleted", nullable = false)
  private boolean deleted;

  @Column(name = "deleted_at", nullable = true)
  private LocalDateTime deletedAt;

  public void markDeleted() {
    this.deleted = true;
    this.deletedAt = LocalDateTime.now();
  }

  public void restore() {
    this.deleted = false;
  }
}

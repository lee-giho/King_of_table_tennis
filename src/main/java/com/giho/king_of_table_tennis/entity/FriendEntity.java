package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "friend")
public class FriendEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "friend_id", nullable = false)
  private String friendId;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private FriendStatus status;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;
}

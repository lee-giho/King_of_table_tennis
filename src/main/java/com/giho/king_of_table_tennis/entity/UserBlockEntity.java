package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_block")
public class UserBlockEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "blocker_id", nullable = false)
  private String blockerId;

  @Column(name = "blocked_id", nullable = false)
  private String blockedId;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;
}

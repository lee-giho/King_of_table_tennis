package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "game_info")
public class GameInfoEntity {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "game_set", nullable = false)
  private int gameSet;

  @Column(name = "target_score", nullable = false)
  private int targetScore;

  @Column(name = "place", nullable = false)
  private String place;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private GameType type;

  @Column(name = "date", nullable = false)
  private LocalDateTime date;
}

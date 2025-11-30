package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_state")
public class GameStateEntity {

  @Id
  @Column(name = "game_info_id", nullable = false, unique = true)
  private String gameInfoId;

  @Column(name = "defender_id", nullable = false)
  private String defenderId;

  @Column(name = "challenger_id", nullable = true)
  private String challengerId;

  @Column(name = "state", nullable = false)
  @Enumerated(EnumType.STRING)
  private GameState state;
}

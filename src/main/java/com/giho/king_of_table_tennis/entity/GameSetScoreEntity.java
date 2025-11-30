package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "game_set_score")
public class GameSetScoreEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "game_info_id", nullable = false)
  private String gameInfoId;

  @Column(name = "set_number", nullable = false)
  private int setNumber;

  @Column(name = "defender_score", nullable = false)
  private int defenderScore;

  @Column(name = "challenger_score", nullable = false)
  private int challengerScore;
}

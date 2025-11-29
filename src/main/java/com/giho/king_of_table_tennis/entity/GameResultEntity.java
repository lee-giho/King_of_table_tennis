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
@Table(name = "game_result")
public class GameResultEntity {

  @Id
  @Column(name = "game_info_id", nullable = false)
  private String gameInfoId;

  @Column(name = "defender_set_score", nullable = false)
  private int defenderSetScore;

  @Column(name = "challenger_set_score", nullable = false)
  private int challengerSetScore;

  @Column(name = "winner_id", nullable = false)
  private String winnerId;

  @Column(name = "loser_id", nullable = false)
  private String loserId;

  @Column(name = "finished_at", updatable = false, insertable = false)
  private LocalDateTime finishedAt;
}

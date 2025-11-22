package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_ranking")
public class UserRankingEntity {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "rating", nullable = false)
  private int rating = 1000;

  @Column(name = "total_games", nullable = false)
  private int totalGames = 0;

  @Column(name = "win_count", nullable = false)
  private int winCount = 0;

  @Column(name = "defeat_count", nullable = false)
  private int defeatCount = 0;

  @Column(name = "win_rate", nullable = false)
  private double winRate = 0.0;

  @Column(name = "last_game_at", nullable = true)
  private LocalDateTime lastGameAt;

  @Column(name = "last_updated_at", insertable = false, updatable = false)
  private LocalDateTime lastUpdatedAt;
}

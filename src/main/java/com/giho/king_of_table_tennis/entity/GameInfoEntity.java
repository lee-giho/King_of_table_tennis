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

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "game_set", nullable = false)
  private int gameSet;

  @Column(name = "game_score", nullable = false)
  private int gameScore;

  @Column(name = "place", nullable = false)
  private String place;

  @Column(name = "acceptance_type", nullable = false)
  @Enumerated(EnumType.STRING)
  private AcceptanceType acceptanceType;

  @Column(name = "game_date", nullable = false)
  private LocalDateTime gameDate;
}

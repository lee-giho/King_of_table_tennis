package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GameStats {
  private int totalGames;
  private int winCount;
  private int defeatCount;
  private double winRate;
}

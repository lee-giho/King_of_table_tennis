package com.giho.king_of_table_tennis.dto;

public interface GameStatsProjection {
  Integer getTotalGames();
  Integer getWinCount();
  Integer getDefeatCount();
  Double getWinRate();
}

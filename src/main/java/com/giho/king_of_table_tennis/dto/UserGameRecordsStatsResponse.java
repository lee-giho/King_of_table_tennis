package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserGameRecordsStatsResponse {
  private GameStats totalStats;
  private GameStats recentStats;
}
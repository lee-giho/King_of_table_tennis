package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 전적 정보 DTO")
public class GameRecordInfo {
  private GameUserInfo myInfo;
  private GameUserInfo opponentInfo;
  private String gameInfoId;
  private LocalDateTime gameDate;
  private String place;
  private boolean isWin;
}

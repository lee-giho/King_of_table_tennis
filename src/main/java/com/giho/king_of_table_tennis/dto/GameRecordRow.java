package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GameRecordRow {
  private String gameInfoId;

  private String myId;
  private String myNickName;
  private String myProfileImage;
  private String myRacketType;
  private Integer mySetScore;

  private String opponentId;
  private String opponentNickName;
  private String opponentProfileImage;
  private String opponentRacketType;
  private Integer opponentSetScore;

  private String place;
  private LocalDateTime gameDate;
}

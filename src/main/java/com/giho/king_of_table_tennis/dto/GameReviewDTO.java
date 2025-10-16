package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "경기 리뷰 정보를 반환하는 DTO")
public class GameReviewDTO {
  private String id;
  private UserInfo reviewee;
  private GameInfoDTO gameInfo;

  // 기술 관련 점수
  private int scoreServe;
  private int scoreReceive;
  private int scoreRally;
  private int scoreStrokes;
  private int scoreStrategy;

  // 매너 관련 점수
  private int scoreManner;
  private int scorePunctuality;
  private int scoreCommunity;
  private int scorePoliteness;
  private int scoreRematch;

  private String comment;

  private LocalDateTime writeDate;
}

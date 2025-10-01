package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 후 작성한 리뷰 등록 요청 DTO")
public class RegisterReviewRequestDTO {
  private String revieweeId;
  private int scoreServe;
  private int scoreReceive;
  private int scoreRally;
  private int scoreStrokes;
  private int scoreStrategy;
  private int scoreManner;
  private int scorePunctuality;
  private int scoreCommunity;
  private int scorePoliteness;
  private int scoreRematch;
  private String comment;
}

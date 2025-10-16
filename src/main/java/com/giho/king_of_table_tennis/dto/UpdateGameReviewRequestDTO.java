package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 후 작성한 리뷰 수정 요청 DTO")
public class UpdateGameReviewRequestDTO {
  private Integer scoreServe;
  private Integer scoreReceive;
  private Integer scoreRally;
  private Integer scoreStrokes;
  private Integer scoreStrategy;

  private Integer scoreManner;
  private Integer scorePunctuality;
  private Integer scoreCommunity;
  private Integer scorePoliteness;
  private Integer scoreRematch;

  private String comment;
}

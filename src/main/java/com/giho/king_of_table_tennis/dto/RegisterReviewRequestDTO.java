package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 후 작성한 리뷰 등록 요청 DTO")
public class RegisterReviewRequestDTO {
  private String revieweeId;
  @Min(1) @Max(5) private int scoreServe;
  @Min(1) @Max(5) private int scoreReceive;
  @Min(1) @Max(5) private int scoreRally;
  @Min(1) @Max(5) private int scoreStrokes;
  @Min(1) @Max(5) private int scoreStrategy;
  @Min(1) @Max(5) private int scoreManner;
  @Min(1) @Max(5) private int scorePunctuality;
  @Min(1) @Max(5) private int scoreCommunity;
  @Min(1) @Max(5) private int scorePoliteness;
  @Min(1) @Max(5) private int scoreRematch;
  private String comment;
}

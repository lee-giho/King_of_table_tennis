package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "경기 종료 시 결과")
public class FinishGameRequest {
  // 경기 세트 점수
  private int defenderSetScore;
  private int challengerSetScore;

  // 각 세트별 점수
  private List<SetScoreDTO> sets;
}

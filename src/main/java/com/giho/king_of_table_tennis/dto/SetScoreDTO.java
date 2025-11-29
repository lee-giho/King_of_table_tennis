package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "경기 중 한 세트 점수")
public class SetScoreDTO {
  private int setNumber;
  private int defenderScore;
  private int challengerScore;
}

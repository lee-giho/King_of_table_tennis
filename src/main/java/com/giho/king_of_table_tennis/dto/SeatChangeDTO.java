package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 중 자리바꿈 정보 DTO")
public class SeatChangeDTO {
  private boolean leftIsDefender;
}

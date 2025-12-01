package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.AcceptanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "게임 만들기 요청 DTO")
public class CreateGameRequestDTO {
  private String title;
  private int gameSet;
  private int gameScore;
  private String place;
  private AcceptanceType acceptanceType;
  private LocalDateTime gameDate;
}

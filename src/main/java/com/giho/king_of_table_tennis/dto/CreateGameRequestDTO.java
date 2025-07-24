package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.GameState;
import com.giho.king_of_table_tennis.entity.GameType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "게임 만들기 요청 DTO")
public class CreateGameRequestDTO {
  private int gameSet;
  private int targetScore;
  private String place;
  private GameType type;
  private LocalDateTime date;
}

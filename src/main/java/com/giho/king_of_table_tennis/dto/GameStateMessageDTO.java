package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.GameState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GameStateMessageDTO {
  private String gameInfoId;
  private GameState state;
}

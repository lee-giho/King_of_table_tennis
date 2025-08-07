package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "참가자와 경기 정보 DTO")
public class GameDetailInfo {
  private UserInfo defenderInfo;
  private UserInfo challengerInfo;
  private GameInfoEntity gameInfo;
  private GameStateEntity gameState;
}

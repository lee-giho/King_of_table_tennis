package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import com.giho.king_of_table_tennis.entity.GameState;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 정보 DTO")
public class RecruitingGameDTO {
  private GameInfoEntity gameInfo;
  private String creatorId;
  private GameState gameState;
  private boolean isMine;
  private boolean alreadyApplied;

  public RecruitingGameDTO(GameInfoEntity gameInfo, String creatorId, GameState gameState) {
    this.gameInfo = gameInfo;
    this.creatorId = creatorId;
    this.gameState = gameState;
    this.isMine = false; // 기본값
    this.alreadyApplied = false; // 기본값
  }
}

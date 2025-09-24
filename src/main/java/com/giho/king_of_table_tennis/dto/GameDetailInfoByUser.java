package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.GameStateEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "참가자와 경기 정보, 내가 만든 경기인지, 신청자가 몇 명인지 알 수 있는 DTO")
public class GameDetailInfoByUser {
  private UserInfo defenderInfo;
  private UserInfo challengerInfo;
  private GameInfoDTO gameInfo;
  private GameStateEntity gameState;
  private boolean isMine;
  private long applicationCount;
}
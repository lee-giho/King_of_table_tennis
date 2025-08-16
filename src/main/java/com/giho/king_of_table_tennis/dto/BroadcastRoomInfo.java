package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "경기 방송 방 정보 DTO")
public class BroadcastRoomInfo {
  private String gameInfoId;
  private String roomName;
  private GameUserInfo defender;
  private GameUserInfo challenger;
  private LocalDateTime createdAt;

  public BroadcastRoomInfo(String gameInfoId, String roomName, GameUserInfo defender, GameUserInfo challenger) {
    this.gameInfoId = gameInfoId;
    this.roomName = roomName;
    this.defender = defender;
    this.challenger = challenger;
    this.createdAt = LocalDateTime.now();
  }
}

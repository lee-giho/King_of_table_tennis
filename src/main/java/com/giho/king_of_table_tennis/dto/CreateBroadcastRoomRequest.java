package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 방송 방 만들기 위한 요청 DTO")
public class CreateBroadcastRoomRequest {
  private String gameInfoId;
}

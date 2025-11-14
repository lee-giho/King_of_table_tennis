package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "친구 요청 DTO")
public class FriendRequestDTO {
  private String receiverId;
}

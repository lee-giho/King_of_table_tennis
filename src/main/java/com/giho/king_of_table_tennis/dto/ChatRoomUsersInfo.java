package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "채팅에 참여하고 있는 두 사용자 정보 DTO")
public class ChatRoomUsersInfo {
  private UserInfo myInfo;
  private UserInfo friendInfo;
}

package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.dto.enums.FriendRequestAnswerType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "친구 요청 응답 타입 DTO")
public class FriendRequestAnswerDTO {
  private FriendRequestAnswerType answer;
}
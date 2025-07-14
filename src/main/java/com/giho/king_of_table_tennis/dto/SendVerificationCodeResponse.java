package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendVerificationCodeResponse {
  @Schema(description = "세션 아이디", example = "C9D663E9839884111CC7CB0DF1C82177")
  private String sessionId;
}

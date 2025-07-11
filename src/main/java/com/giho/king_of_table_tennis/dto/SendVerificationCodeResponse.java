package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SendVerificationCodeResponse {
  private String sessionId;
}

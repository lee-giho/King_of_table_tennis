package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
  private boolean isFirstLogin;
}

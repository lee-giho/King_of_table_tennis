package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "로그인 응답 DTO")
public class LoginResponseDTO {
  private String accessToken;
  private String refreshToken;
  private boolean isFirstLogin;
}

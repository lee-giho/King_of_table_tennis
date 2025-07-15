package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 찾기(변경) DTO")
public class ChangePasswordRequest {
  private String id;
  private String password;
  private String name;
  private String email;
}

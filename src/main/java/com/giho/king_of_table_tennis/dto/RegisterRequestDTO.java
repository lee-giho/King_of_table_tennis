package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "회원가입 요청 DTO")
public class RegisterRequestDTO {
  private String id;
  private String password;
  private String name;
  private String nickName;
  private String email;
  private String profileImage;
}

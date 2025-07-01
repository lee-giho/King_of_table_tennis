package com.giho.king_of_table_tennis.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterDTO {
  private String id;
  private String password;
  private String name;
  private String nickName;
  private String email;
  private String profileImage;
}

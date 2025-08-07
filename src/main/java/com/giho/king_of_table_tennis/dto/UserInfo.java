package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "민감한 정보를 제외한 사용자 DTO")
public class UserInfo {
  private String id;
  private String name;
  private String nickName;
  private String email;
  private String profileImage;
  private String racketType;
  private String userLevel;
  private int winCount;
  private int defeatCount;
}

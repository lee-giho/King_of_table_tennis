package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "마이페이지에서 간단한 내 정보를 보여주기 위한 DTO")
public class MySimpleInfoResponse {
  private String nickName;
  private String profileImage;
  private String racketType;
  private int winCount;
  private int defeatCount;
}

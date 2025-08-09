package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 방송 중 사용되는 사용자 DTO")
public class GameUserInfo {
  private String id;
  private String nickName;
  private String profileImage;
  private String racketType;
  private int setScore;
  private int score;

  public GameUserInfo(String id, String nickName, String profileImage, String racketType) {
    this.id = id;
    this.nickName = nickName;
    this.profileImage = profileImage;
    this.racketType = racketType;
    this.setScore = 0;
    this.score = 0;
  }
}

package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.FriendStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "민감한 정보를 제외한 사용자 DTO")
public class UserInfo {
  // 기본 정보
  private String id;
  private String name;
  private String nickName;
  private String email;
  private String profileImage;

  // 탁구 정보
  private String racketType;
  private String userLevel;

  // 랭킹 정보
  private int rating;
  private double winRate;
  private int totalGames;
  private int winCount;
  private int defeatCount;
  private LocalDateTime lastGameAt;

  private FriendStatus friendStatus;
}

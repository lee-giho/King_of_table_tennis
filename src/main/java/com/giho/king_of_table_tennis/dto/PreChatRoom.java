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
@Schema(description = "채팅 상대 정보를 포함한 채팅방 미리보기 DTO")
public class PreChatRoom {
  private String id;

  private UserInfo friend;

  private LocalDateTime createdAt;

  private String lastMessage;
  private LocalDateTime lastSentAt;

  private int unreadCount;

  public PreChatRoom(
    String id,
    String participantId, String participantName, String participantNickName, String participantEmail, String participantProfileImage,
    String racketType, String userLevel,
    int rating, double winRate, int totalGames, Integer winCount, Integer defeatCount, LocalDateTime lastGameAt,
    FriendStatus friendStatus,
    LocalDateTime createdAt,
    String lastMessage, LocalDateTime lastSentAt
  ) {
    this.id = id;
    this.friend = new UserInfo(
      participantId, participantName, participantNickName, participantEmail, participantProfileImage,
      racketType, userLevel,
      rating, winRate, totalGames, winCount, defeatCount, lastGameAt,
      friendStatus
    );
    this.createdAt = createdAt;
    this.lastMessage = lastMessage;
    this.lastSentAt = lastSentAt;
    this.unreadCount = 0;
  }
}

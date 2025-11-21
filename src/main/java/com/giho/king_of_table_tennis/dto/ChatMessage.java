package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "채팅 메시지 DTO")
public class ChatMessage {
  private Long id;
  private String roomId;
  private String senderId;
  private String content;
  private LocalDateTime sentAt;

  private int unreadCount;
}

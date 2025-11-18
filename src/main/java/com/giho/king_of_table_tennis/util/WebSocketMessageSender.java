package com.giho.king_of_table_tennis.util;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.event.ReadMessageEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketMessageSender {

  private final SimpMessagingTemplate messagingTemplate;

  public void sendChatMessage(ChatMessage payload) {
    messagingTemplate.convertAndSend(
      "/topic/chat/room/" + payload.getRoomId(),
      payload
    );
  }

  public void sendReadReceipt(ReadMessageEvent readMessageEvent) {
    messagingTemplate.convertAndSend(
      "/topic/chat/read/" + readMessageEvent.roomId(),
      readMessageEvent
    );
  }
}

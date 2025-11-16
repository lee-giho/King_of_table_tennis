package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.dto.SendMessagePayload;
import com.giho.king_of_table_tennis.service.ChatMessageService;
import com.giho.king_of_table_tennis.util.WebSocketMessageSender;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ChatMessage-Controller", description = "채팅 웹소켓 관련 API")
public class ChatWebSocketController {

  private final ChatMessageService chatMessageService;

  private final WebSocketMessageSender webSocketMessageSender;

  @MessageMapping("/chat/send")
  public void sendMessage(
    @Header("Authorization") String token,
    SendMessagePayload sendMessagePayload
  ) {
    ChatMessage chatMessage = chatMessageService.sendMessage(token, sendMessagePayload);

    webSocketMessageSender.sendChatMessage(chatMessage);
  }
}

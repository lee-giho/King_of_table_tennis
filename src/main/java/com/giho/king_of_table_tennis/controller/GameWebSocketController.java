package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.GameStateMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

  private final SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/state")
  public void handleGameState(GameStateMessageDTO message) {
    String destination = "/topic/game/state/" + message.getGameInfoId();
    messagingTemplate.convertAndSend(destination, message);
  }
}

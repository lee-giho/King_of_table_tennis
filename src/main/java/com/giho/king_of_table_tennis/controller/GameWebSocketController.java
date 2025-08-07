package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.GameStateMessageDTO;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class GameWebSocketController {

  private final SimpMessagingTemplate messagingTemplate;

  private final GameStateRepository gameStateRepository;

  @MessageMapping("/state")
  public void handleGameState(GameStateMessageDTO message) {

    GameStateEntity gameStateEntity = gameStateRepository.findByGameInfoId(message.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));

    gameStateEntity.setState(message.getState());

    gameStateRepository.save(gameStateEntity);

    String destination = "/topic/game/state/" + message.getGameInfoId();
    messagingTemplate.convertAndSend(destination, message);
  }
}

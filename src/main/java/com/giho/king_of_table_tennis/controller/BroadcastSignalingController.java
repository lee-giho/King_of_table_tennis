package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.GameUserInfo;
import com.giho.king_of_table_tennis.dto.SeatChangeDTO;
import com.giho.king_of_table_tennis.dto.UpdateScoreRequest;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.BroadcastRoomRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BroadcastSignalingController {

  private final SimpMessagingTemplate messagingTemplate;
  private final BroadcastRoomRepository broadcastRoomRepository;

  @MessageMapping("/broadcast/peer/offer/{roomId}")
  public void handleOffer(@Payload String offer,
                          @DestinationVariable(value = "roomId") String roomId) {
    log.info("[BROADCAST Offer] roomId={}, offer={}", roomId, offer);
    messagingTemplate.convertAndSend("/topic/broadcast/peer/offer/" + roomId, offer);
  }

  @MessageMapping("/broadcast/peer/answer/{roomId}/{viewerId}")
  public void handleAnswer(@Payload String answer,
                           @DestinationVariable(value = "roomId") String roomId,
                           @DestinationVariable(value = "viewerId") String viewerId) {
    log.info("[BROADCAST Answer] roomId={}, viewerId={}, answer={}", roomId, viewerId, answer);
    messagingTemplate.convertAndSend("/topic/broadcast/peer/answer/" + roomId + "/" + viewerId, answer);
  }

  @MessageMapping("/broadcast/peer/candidate/{roomId}")
  public void handleCandidateFromViewer(@Payload String candidate,
                                        @DestinationVariable(value = "roomId") String roomId) {
    log.info("[BROADCAST CandidateFromViewer] roomId={}, candidate={}", roomId, candidate);
    messagingTemplate.convertAndSend("/topic/broadcast/peer/candidate/" + roomId, candidate);
  }

  @MessageMapping("/broadcast/peer/candidate/viewer/{roomId}")
  public void handleCandidateToViewer(@Payload String candidate,
                                      @DestinationVariable(value = "roomId") String roomId) {
    log.info("[BROADCAST CandidateToViewer] roomId={}, candidate={}", roomId, candidate);
    messagingTemplate.convertAndSend("/topic/broadcast/peer/candidate/viewer/" + roomId, candidate);
  }

  @MessageMapping("/broadcast/end/{roomId}")
  @SendTo("/topic/broadcast/end/{roomId}")
  public String endBroadcast(@Payload String message,
                             @DestinationVariable(value = "roomId") String roomId) {
    log.info("[BROADCAST End] roomId={}", roomId);
    return message;
  }

  @MessageMapping("/broadcast/score/{roomId}")
  @SendTo("/topic/broadcast/score/{roomId}")
  public UpdateScoreRequest updateScore(@Payload UpdateScoreRequest updateScoreRequest, @DestinationVariable(value = "roomId") String roomId) {
    broadcastRoomRepository.patchRoom(roomId, room -> {
      GameUserInfo target = "defender".equalsIgnoreCase(updateScoreRequest.getSide())
        ? room.getDefender()
        : room.getChallenger();
      if (target == null) throw new CustomException(ErrorCode.BROADCAST_PLAYER_NOT_FOUND);
      target.setScore(updateScoreRequest.getNewScore());
    });
    return updateScoreRequest;
  }

  @MessageMapping("/broadcast/leftIsDefender/{roomId}")
  @SendTo("/topic/broadcast/leftIsDefender/{roomId}")
  public SeatChangeDTO changeSeats(@Payload SeatChangeDTO seatChangeDTO, @DestinationVariable(value = "roomId") String roomId) {
    broadcastRoomRepository.patchRoom(roomId, room -> {
      room.setLeftIsDefender(seatChangeDTO.isLeftIsDefender());
    });
    return seatChangeDTO;
  }
}

package com.giho.king_of_table_tennis.util;

import com.giho.king_of_table_tennis.dto.PreChatRoom;
import com.giho.king_of_table_tennis.dto.RoomUnreadCount;
import com.giho.king_of_table_tennis.event.PreChatRoomUpdatedEvent;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.ChatMessageRepository;
import com.giho.king_of_table_tennis.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventHandler {

  private final ChatRoomRepository chatRoomRepository;

  private final ChatMessageRepository chatMessageRepository;

  private final WebSocketMessageSender webSocketMessageSender;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePreChatRoomUpdated(PreChatRoomUpdatedEvent event) {
    String roomId = event.roomId();
    String creatorId = event.creatorId();
    String participantId = event.participantId();

    PreChatRoom forCreator = chatRoomRepository.findPreChatRoom(roomId, creatorId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
    PreChatRoom forParticipant = chatRoomRepository.findPreChatRoom(roomId, participantId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    List<String> singleRoomId = List.of(roomId);

    // creator 기준
    long creatorUnread = chatMessageRepository.countUnreadMessagesByRoomId(singleRoomId, creatorId)
      .stream()
      .findFirst()
      .map(RoomUnreadCount::getUnreadCount)
      .orElse(0L);

    // participant 기준
    long participantUnread = chatMessageRepository.countUnreadMessagesByRoomId(singleRoomId, participantId)
      .stream()
      .findFirst()
      .map(RoomUnreadCount::getUnreadCount)
      .orElse(0L);

    forCreator.setUnreadCount((int) creatorUnread);
    forParticipant.setUnreadCount((int) participantUnread);

    webSocketMessageSender.sendPreChatRoom(
      forCreator,
      forParticipant,
      creatorId,
      participantId
    );
  }
}

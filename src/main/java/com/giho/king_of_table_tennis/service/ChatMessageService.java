package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.dto.SendMessagePayload;
import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.jwt.JWTUtil;
import com.giho.king_of_table_tennis.repository.ChatMessageRepository;
import com.giho.king_of_table_tennis.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;

  private final ChatRoomRepository chatRoomRepository;

  private final JWTUtil jwtUtil;

  @Transactional
  public ChatMessage sendMessage(String token, SendMessagePayload sendMessagePayload) {

    String senderId = jwtUtil.getUserId(jwtUtil.getTokenWithoutBearer(token));
    String roomId = sendMessagePayload.getRoomId();
    String content = sendMessagePayload.getContent();

    // 채팅방 확인
    ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    // 채팅방에 참여중인지 확인
    if (!chatRoomEntity.getCreatorId().equals(senderId) && !chatRoomEntity.getParticipantId().equals(senderId)) {
      throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
    }

    // 메시지 저장
    ChatMessageEntity savedChatMessageEntity = chatMessageRepository.save(
      ChatMessageEntity.builder()
        .roomId(roomId)
        .senderId(senderId)
        .content(content)
        .build()
    );

    // 채팅방 last_message와 last_sent_at 업데이트
    chatRoomEntity.updateLastmessage(savedChatMessageEntity.getContent(), savedChatMessageEntity.getSentAt());

    return ChatMessage.builder()
      .id(savedChatMessageEntity.getId())
      .roomId(savedChatMessageEntity.getRoomId())
      .senderId(savedChatMessageEntity.getSenderId())
      .content(savedChatMessageEntity.getContent())
      .sentAt(savedChatMessageEntity.getSentAt())
      .build();
  }
}

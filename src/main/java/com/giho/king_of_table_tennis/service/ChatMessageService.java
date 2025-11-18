package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.ReadMessagePayload;
import com.giho.king_of_table_tennis.dto.SendMessagePayload;
import com.giho.king_of_table_tennis.entity.ChatMessageEntity;
import com.giho.king_of_table_tennis.entity.ChatReadStateEntity;
import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import com.giho.king_of_table_tennis.event.ReadMessageEvent;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.jwt.JWTUtil;
import com.giho.king_of_table_tennis.repository.ChatMessageRepository;
import com.giho.king_of_table_tennis.repository.ChatReadStateRepository;
import com.giho.king_of_table_tennis.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatMessageRepository chatMessageRepository;

  private final ChatRoomRepository chatRoomRepository;

  private final ChatReadStateRepository chatReadStateRepository;

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

  @Transactional(readOnly = true)
  public PageResponse<ChatMessage> getMessages(String roomId, int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String myId = authentication.getName();

    // 채팅방 확인
    ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    // 채팅방에 참여중인지 확인
    if (!chatRoomEntity.getCreatorId().equals(myId) && !chatRoomEntity.getParticipantId().equals(myId)) {
      throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
    }

    String friendId = chatRoomEntity.getCreatorId().equals(myId)
      ? chatRoomEntity.getParticipantId()
      : chatRoomEntity.getCreatorId();

    Long friendLastReadId = chatReadStateRepository.findByRoomIdAndUserId(roomId, friendId)
      .map(ChatReadStateEntity::getLastReadMessageId)
      .orElse(null);

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "sentAt"));
    Page<ChatMessageEntity> messagePage = chatMessageRepository.findByRoomId(roomId, pageable);

    List<ChatMessage> content = messagePage.getContent().stream()
      .sorted(Comparator.comparing(ChatMessageEntity::getSentAt))
      .map(e -> {
        int unreadCount = 0;
        if (e.getSenderId().equals(myId)) {
          boolean notRead = (friendLastReadId == null) || (friendLastReadId < e.getId());
          unreadCount = notRead ? 1 : 0;
        }
        return ChatMessage.builder()
          .id(e.getId())
          .roomId(e.getRoomId())
          .senderId(e.getSenderId())
          .content(e.getContent())
          .sentAt(e.getSentAt())
          .unreadCount(unreadCount)
          .build();
      }).toList();

    return new PageResponse<>(
      content,
      messagePage.getTotalPages(),
      messagePage.getTotalElements(),
      messagePage.getNumber(),
      messagePage.getSize()
    );
  }

  @Transactional
  public ReadMessageEvent readMessage(String token, ReadMessagePayload readMessagePayload) {
    String userId = jwtUtil.getUserId(jwtUtil.getTokenWithoutBearer(token));
    String roomId = readMessagePayload.getRoomId();
    Long clientLastReadId = readMessagePayload.getLastReadMessageId();

    // 채팅방 확인
    ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(roomId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    // 채팅방에 참여중인지 확인
    if (!chatRoomEntity.getCreatorId().equals(userId) && !chatRoomEntity.getParticipantId().equals(userId)) {
      throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
    }

    // lastReadMessageId 결정
    Long lastReadMessageId;
    if (clientLastReadId != null) {
      lastReadMessageId = clientLastReadId;
    } else {
      // 클라이언트에서 안 보냈을 경우, DB에서 room의 최신 메시지를 기준으로 읽음 처리
      lastReadMessageId = chatMessageRepository.findTopByRoomIdOrderByIdDesc(roomId)
        .map(ChatMessageEntity::getId)
        .orElse(null);
    }

    if (lastReadMessageId == null) {
      // 방에 메시지가 없으면 갱신 X
      return new ReadMessageEvent(roomId, userId, null, null);
    }

    ChatReadStateEntity chatReadStateEntity = chatReadStateRepository.findByRoomIdAndUserId(roomId, userId)
      .orElseGet(() -> {
        ChatReadStateEntity s = new ChatReadStateEntity();
        s.setRoomId(roomId);
        s.setUserId(userId);
        return s;
      });

    // 기존 값보다 뒤에 있는 메시지면 갱신
    if (chatReadStateEntity.getLastReadMessageId() == null || chatReadStateEntity.getLastReadMessageId() < lastReadMessageId) {
      chatReadStateEntity.setLastReadMessageId(lastReadMessageId);
      chatReadStateEntity.setLastReadAt(LocalDateTime.now());
      chatReadStateRepository.save(chatReadStateEntity);
    }

    return new ReadMessageEvent(
      roomId,
      userId,
      chatReadStateEntity.getLastReadMessageId(),
      chatReadStateEntity.getLastReadAt()
    );
  }
}

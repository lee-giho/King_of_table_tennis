package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.CreateChatRoomRequest;
import com.giho.king_of_table_tennis.dto.CreateChatRoomResponse;
import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.ChatRoomRepository;
import com.giho.king_of_table_tennis.repository.FriendRepository;
import com.giho.king_of_table_tennis.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  private final UserBlockRepository userBlockRepository;

  private final FriendRepository friendRepository;

  @Transactional
  public CreateChatRoomResponse createOrGetChatRoom(CreateChatRoomRequest createChatRoomRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();
    String targetUserId = createChatRoomRequest.getTargetUserId();

    if (userId.equals(targetUserId)) { // 자기 자신 채팅 X
      throw new CustomException(ErrorCode.SELF_CHAT_NOT_ALLOWED);
    }

    boolean isBlock = userBlockRepository.existsByBlockerIdAndBlockedId(userId, targetUserId) || userBlockRepository.existsByBlockerIdAndBlockedId(targetUserId, userId);
    if (isBlock) { // 차단 관계인지 확인
      throw new CustomException(ErrorCode.CHAT_BLOCKED_RELATION);
    }

    boolean isFriend = friendRepository.existsMutualFriendRelation(userId, targetUserId);
    if (!isFriend) { // 친구 관계인지 확인
      throw new CustomException(ErrorCode.STATUS_NOT_FRIEND);
    }

    return chatRoomRepository.findByCreatorIdAndParticipantIdOrCreatorIdAndParticipantId(
        userId, targetUserId,
        targetUserId, userId
      ).map(room -> new CreateChatRoomResponse(room.getId())) // 기존 방이 있으면 해당 방의 아이디 응답
      .orElseGet(() -> { // 없으면 새로 방을 만들고 해당 방의 아이디 응답
        ChatRoomEntity newChatRoomEntity = new ChatRoomEntity();

        newChatRoomEntity.setId(UUID.randomUUID().toString());
        newChatRoomEntity.setCreatorId(userId);
        newChatRoomEntity.setParticipantId(targetUserId);

        ChatRoomEntity savedChatRoomEntity = chatRoomRepository.save(newChatRoomEntity);

        return new CreateChatRoomResponse(savedChatRoomEntity.getId());
      });
  }
}

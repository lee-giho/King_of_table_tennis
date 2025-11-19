package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.ChatRoomEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;

  private final UserBlockRepository userBlockRepository;

  private final FriendRepository friendRepository;

  private final UserRepository userRepository;

  private final ChatMessageRepository chatMessageRepository;

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

  public PageResponse<PreChatRoom> getMyChatRooms(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Pageable pageable = PageRequest.of(page, size);

    Page<PreChatRoom> chatRoomPage = chatRoomRepository.findMyPreChatRooms(userId, pageable);
    List<PreChatRoom> chatRooms = chatRoomPage.getContent();

    // 방 ID 목록
    List<String> chatRoomIds = chatRooms.stream()
      .map(PreChatRoom::getId)
      .toList();

    if (chatRoomIds.isEmpty()) {
      // 채팅방이 없으면 unreadCount를 0인 상태로 리턴
      return new PageResponse<>(
        chatRooms,
        chatRoomPage.getTotalPages(),
        chatRoomPage.getTotalElements(),
        chatRoomPage.getNumber(),
        chatRoomPage.getSize()
      );
    }

    // 채팅방 목록 전체에 대해 한 번에 unreadCount 계산
    List<RoomUnreadCount> unreadRaw = chatMessageRepository.countUnreadMessagesByRoomId(chatRoomIds, userId);

    // roomId -> unreadCount 맵으로 변환
    Map<String, Long> unreadMap = unreadRaw.stream()
      .collect(Collectors.toMap(
        RoomUnreadCount::getRoomId,
        RoomUnreadCount::getUnreadCount
      ));

    // PreChatRoom DTO에 unreadCount 반영
    List<PreChatRoom> content = chatRooms.stream()
      .peek(room -> {
        long unread = unreadMap.getOrDefault(room.getId(), 0L);
        room.setUnreadCount((int) unread);
      })
      .toList();

    return new PageResponse<>(
      content,
      chatRoomPage.getTotalPages(),
      chatRoomPage.getTotalElements(),
      chatRoomPage.getNumber(),
      chatRoomPage.getSize()
    );
  }

  public ChatRoomUsersInfo getChatRoomUsersInfo(String chatRoomId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    // 채팅방 조회
    ChatRoomEntity chatRoomEntity = chatRoomRepository.findById(chatRoomId)
      .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

    String creatorId = chatRoomEntity.getCreatorId();
    String participantId = chatRoomEntity.getParticipantId();

    // 채팅방에 속해있는지 확인
    if (!creatorId.equals(userId) && !participantId.equals(userId)) {
      throw new CustomException(ErrorCode.CHAT_ROOM_ACCESS_DENIED);
    }

    String friendId = creatorId.equals(userId) ? participantId : creatorId;

    // 채팅방에 참여하고 있는 사람들의 UserInfo 조회
    List<String> userIds = List.of(userId, friendId);
    List<UserInfo> userInfos = userRepository.findUserInfoByIds(userIds, userId);

    Map<String, UserInfo> UserInfoById = userInfos.stream()
      .collect(Collectors.toMap(UserInfo::getId, u -> u));

    UserInfo myInfo = UserInfoById.get(userId);
    UserInfo friendInfo = UserInfoById.get(friendId);

    if (myInfo == null || friendInfo == null) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    return new ChatRoomUsersInfo(
      myInfo,
      friendInfo
    );
  }
}

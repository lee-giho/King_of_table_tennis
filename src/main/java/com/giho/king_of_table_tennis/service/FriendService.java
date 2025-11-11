package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.dto.enums.FriendRequestAnswerType;
import com.giho.king_of_table_tennis.entity.FriendEntity;
import com.giho.king_of_table_tennis.entity.FriendStatus;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.FriendRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendService {

  private final FriendRepository friendRepository;

  private final UserRepository userRepository;

  @Transactional
  public void requestFriend(FriendRequestDTO friendRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String senderId = authentication.getName();

    String receiverId = friendRequestDTO.getReceiverId();

    if (!userRepository.existsById(senderId) && !userRepository.existsById(receiverId)) {
      throw new CustomException(ErrorCode.USER_NOT_FOUND);
    }

    FriendEntity senderFriend = new FriendEntity();

    senderFriend.setId(UUID.randomUUID().toString());
    senderFriend.setUserId(senderId);
    senderFriend.setFriendId(receiverId);
    senderFriend.setStatus(FriendStatus.REQUESTED);

    FriendEntity receiverFriend = new FriendEntity();

    receiverFriend.setId(UUID.randomUUID().toString());
    receiverFriend.setUserId(receiverId);
    receiverFriend.setFriendId(senderId);
    receiverFriend.setStatus(FriendStatus.RECEIVED);

    friendRepository.save(senderFriend);
    friendRepository.save(receiverFriend);
  }

  public PageResponse<UserInfo> getReceivedFriendRequests(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<UserInfo> receivedFriendRequests = friendRepository.findReceivedFriendRequests(userId, pageable);

    return new PageResponse<>(
      receivedFriendRequests.getContent(),
      receivedFriendRequests.getTotalPages(),
      receivedFriendRequests.getTotalElements(),
      receivedFriendRequests.getNumber(),
      receivedFriendRequests.getSize()
    );
  }

  public CountResponseDTO getFriendRequestCountByFriendStatus(FriendStatus friendStatus) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    CountResponseDTO countResponseDTO = friendRepository.countReceivedFriendRequests(userId, friendStatus);

    return countResponseDTO;
  }

  @Transactional
  public void responseFriendRequest(String targetUserId, FriendRequestAnswerDTO friendRequestAnswerDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    FriendEntity myFriendInfo = friendRepository.findFriendEntityByUserIdAndFriendId(userId, targetUserId)
      .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

    if (myFriendInfo.getStatus() != FriendStatus.RECEIVED) {
      throw new CustomException(ErrorCode.FRIEND_UPDATE_FORBIDDEN);
    }

    FriendEntity targetFriendInfo = friendRepository.findFriendEntityByUserIdAndFriendId(targetUserId, userId)
      .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

    if (friendRequestAnswerDTO.getAnswer() == FriendRequestAnswerType.ACCEPT) {
      myFriendInfo.setStatus(FriendStatus.FRIEND);
      targetFriendInfo.setStatus(FriendStatus.FRIEND);

      friendRepository.save(myFriendInfo);
      friendRepository.save(targetFriendInfo);
    } else {
      friendRepository.delete(myFriendInfo);
      friendRepository.delete(targetFriendInfo);
    }
  }

  public PageResponse<UserInfo> getFriendList(int page, int size) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Pageable pageable = PageRequest.of(page, size);

    Page<UserInfo> myFriends = friendRepository.findMyFriends(userId, pageable);

    return new PageResponse<>(
      myFriends.getContent(),
      myFriends.getTotalPages(),
      myFriends.getTotalElements(),
      myFriends.getNumber(),
      myFriends.getSize()
    );
  }

  @Transactional
  public void deleteFriend(String targetUserId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    boolean exists = friendRepository.existsMutualFriendRelation(userId, targetUserId);

    if (!exists) {
      throw new CustomException(ErrorCode.STATUS_NOT_FRIEND);
    }

    friendRepository.deleteBothRelations(userId, targetUserId);
  }
}

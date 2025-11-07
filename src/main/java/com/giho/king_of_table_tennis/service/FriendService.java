package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.FriendRequestDTO;
import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.FriendEntity;
import com.giho.king_of_table_tennis.entity.FriendStatus;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.FriendRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}

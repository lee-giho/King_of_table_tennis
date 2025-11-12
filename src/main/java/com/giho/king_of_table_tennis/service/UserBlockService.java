package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.BlockUserRequest;
import com.giho.king_of_table_tennis.entity.UserBlockEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.UserBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserBlockService {

  private final UserBlockRepository userBlockRepository;

  @Transactional
  public void blockUser(BlockUserRequest blockUserRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    if (userId.equals(blockUserRequest.getTargetUserId())) {
      throw new CustomException(ErrorCode.SELF_BLOCK_NOT_ALLOWED);
    }

    // 이미 차단된 상태면 그냥 넘김
    if (userBlockRepository.existsByBlockerIdAndBlockedId(userId, blockUserRequest.getTargetUserId())) {
      return;
    }

    UserBlockEntity userBlockEntity = new UserBlockEntity();
    userBlockEntity.setId(UUID.randomUUID().toString());
    userBlockEntity.setBlockerId(userId);
    userBlockEntity.setBlockedId(blockUserRequest.getTargetUserId());
    userBlockRepository.save(userBlockEntity);
  }
}

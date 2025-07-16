package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.CheckExistsResponse;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public CheckExistsResponse checkNickNameDuplication(String nickName) {
    boolean isDuplication = userRepository.existsByNickName(nickName);
    return new CheckExistsResponse(isDuplication);
  }
}

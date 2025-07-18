package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.CheckExistsResponseDTO;
import com.giho.king_of_table_tennis.dto.ProfileRegistrationRequestDTO;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final ImageService imageService;

  public CheckExistsResponseDTO checkNickNameDuplication(String nickName) {
    boolean isDuplication = userRepository.existsByNickName(nickName);
    return new CheckExistsResponseDTO(isDuplication);
  }

  @Transactional
  public BooleanResponseDTO profileRegistration(ProfileRegistrationRequestDTO profileRegistrationRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity user = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    String imageFileName;

    if (profileRegistrationRequestDTO.getProfileImage() != null) {
      imageFileName = imageService.saveImage(profileRegistrationRequestDTO.getProfileImage());
    } else {
      imageFileName = "default";
    }

    try {
      user.setNickName(profileRegistrationRequestDTO.getNickName());
      user.setProfileImage(imageFileName);

      userRepository.save(user);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.DB_SAVE_ERROR);
    }
    return new BooleanResponseDTO(true);
  }
}

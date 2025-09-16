package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.entity.UserTableTennisInfoEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.UserRepository;
import com.giho.king_of_table_tennis.repository.UserTableTennisInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserTableTennisInfoRepository userTableTennisInfoRepository;

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

  public BooleanResponseDTO tableTennisInfoRegistration(TableTennisInfoRegistrationRequestDTO tennisInfoRegistrationRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    System.out.println(tennisInfoRegistrationRequestDTO.getRacketType());
    System.out.println(tennisInfoRegistrationRequestDTO.getUserLevel());

    boolean isExistTableTennisInfo = userTableTennisInfoRepository.existsByUserId(userId);

    if (isExistTableTennisInfo) {
      throw new CustomException(ErrorCode.TABLE_TENNIS_INFO_ALREADY_EXIST);
    }

    UserTableTennisInfoEntity userTableTennisInfoEntity = new UserTableTennisInfoEntity();

    userTableTennisInfoEntity.setUserId(userId);
    userTableTennisInfoEntity.setRacketType(tennisInfoRegistrationRequestDTO.getRacketType());
    userTableTennisInfoEntity.setUserLevel(tennisInfoRegistrationRequestDTO.getUserLevel());

    userTableTennisInfoRepository.save(userTableTennisInfoEntity);

    return new BooleanResponseDTO(true);
  }

  public MySimpleInfoResponse getMySimpleInfo() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    MySimpleInfoResponse mySimpleInfoResponse = userRepository.findMySimpleInfoById(userId);

    return mySimpleInfoResponse;
  }

  public UserInfo getUserInfo () {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserInfo userInfo = userRepository.findUserInfoById(userId);

    return userInfo;
  }
}

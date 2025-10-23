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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  private final UserTableTennisInfoRepository userTableTennisInfoRepository;

  private final ImageService imageService;

  private final PasswordEncoder passwordEncoder;

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

  public UserInfo getMyInfo () {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserInfo userInfo = userRepository.findUserInfoById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return userInfo;
  }

  public BooleanResponseDTO changeNickName(ChangeValueRequest changeValueRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      userEntity.setNickName(changeValueRequest.getChangeValue());
      userRepository.save(userEntity);
      return new BooleanResponseDTO(true);
    } catch (Exception e) {
      return new BooleanResponseDTO(false);
    }
  }

  public BooleanResponseDTO changeRacketType(ChangeValueRequest changeValueRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserTableTennisInfoEntity userTableTennisInfoEntity = userTableTennisInfoRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.TABLE_TENNIS_INFO_NOT_FOUND));

    try {
      userTableTennisInfoEntity.setRacketType(changeValueRequest.getChangeValue());
      userTableTennisInfoRepository.save(userTableTennisInfoEntity);
      return new BooleanResponseDTO(true);
    } catch (Exception e) {
      return new BooleanResponseDTO(false);
    }
  }

  public BooleanResponseDTO verifyPassword(VerifyPasswordRequest verifyPasswordRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    boolean isMatched = passwordEncoder.matches(verifyPasswordRequest.getPassword(), userEntity.getPassword());

    return new BooleanResponseDTO(isMatched);
  }

  public BooleanResponseDTO changePassword(ChangeValueRequest changeValueRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      userEntity.setPassword(passwordEncoder.encode(changeValueRequest.getChangeValue()));
      userRepository.save(userEntity);
      return new BooleanResponseDTO(true);
    } catch (Exception e) {
      return new BooleanResponseDTO(false);
    }
  }

  @Transactional
  public BooleanResponseDTO changeProfileImage(UploadProfileImageRequest uploadProfileImageRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      // 새 이미지 저장
      String newImageName = imageService.saveImage(uploadProfileImageRequest.getProfileImage());
      String oldImageName = userEntity.getProfileImage();

      // DB 업데이트
      userEntity.setProfileImage(newImageName);
      userRepository.save(userEntity);

      // 트랜잭션 완료 후 처리
      TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            if (oldImageName != null && !oldImageName.isEmpty()) {
              imageService.deleteProfileImage(oldImageName);
            }
          }
          @Override
          public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
              // DB 롤백 시 저장했던 새 이미지 삭제
              imageService.deleteProfileImage(newImageName);
            }
          }
        }
      );
      return new BooleanResponseDTO(true);
    } catch (Exception e) {
      return new BooleanResponseDTO(false);
    }
  }

  @Transactional
  public BooleanResponseDTO deleteProfileImage() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    try {
      String oldImageName = userEntity.getProfileImage();
      if (oldImageName == null || oldImageName.isEmpty()) {
        // 원래 프로필 이미지가 없을 때
        return new BooleanResponseDTO(true);
      }

      // DB 업데이트
      userEntity.setProfileImage("default");
      userRepository.save(userEntity);

      // 트랜잭션 완료 후 처리
      TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          @Override
          public void afterCommit() {
            imageService.deleteProfileImage(oldImageName);
          }
        }
      );
      return new BooleanResponseDTO(true);
    } catch (Exception e) {
      System.out.println(e);
      return new BooleanResponseDTO(false);
    }
  }

  public UserInfo getUserInfo(String userId) {
    UserInfo userInfo = userRepository.findUserInfoById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return userInfo;
  }
}

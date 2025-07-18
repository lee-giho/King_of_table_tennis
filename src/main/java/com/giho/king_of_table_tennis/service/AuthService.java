package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.ChangePasswordRequestDTO;
import com.giho.king_of_table_tennis.dto.CheckExistsResponseDTO;
import com.giho.king_of_table_tennis.dto.FindIdResponseDTO;
import com.giho.king_of_table_tennis.dto.RegisterRequestDTO;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public boolean register(RegisterRequestDTO registerDTO) {

    boolean isExistUser = userRepository.existsById(registerDTO.getId());

    if (isExistUser) {
      throw new CustomException(ErrorCode.USER_ALREADY_EXIST);
    }

    UserEntity userEntity = new UserEntity();

    userEntity.setId(registerDTO.getId());
    userEntity.setPassword(bCryptPasswordEncoder.encode(registerDTO.getPassword()));
    userEntity.setName(registerDTO.getName());
    userEntity.setNickName(registerDTO.getNickName());
    userEntity.setEmail(registerDTO.getEmail());
    userEntity.setProfileImage(registerDTO.getProfileImage());
    userEntity.setRole("ROLE_USER");

    userRepository.save(userEntity);

    return true;
  }

  public CheckExistsResponseDTO checkIdDuplication(String id) {

    boolean isDuplication = userRepository.existsById(id);
    return new CheckExistsResponseDTO(isDuplication);
  }

  public CheckExistsResponseDTO checkNickNameDuplication(String nickName) {

    boolean isDuplication = userRepository.existsByNickName(nickName);
    return new CheckExistsResponseDTO(isDuplication);
  }

  public FindIdResponseDTO findId(String name, String email) {
    UserEntity user = userRepository.findByNameAndEmail(name, email)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    return new FindIdResponseDTO(user.getId());
  }

  public boolean findPassword(ChangePasswordRequestDTO changePasswordRequest) {
    try {
      UserEntity user = userRepository.findByIdAndNameAndEmail(changePasswordRequest.getId(), changePasswordRequest.getName(), changePasswordRequest.getEmail())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

      user.setPassword(bCryptPasswordEncoder.encode(changePasswordRequest.getPassword()));
      userRepository.save(user);
      return true;
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return false;
    }
  }
}

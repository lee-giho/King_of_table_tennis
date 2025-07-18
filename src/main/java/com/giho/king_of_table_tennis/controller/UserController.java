package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.CheckExistsResponseDTO;
import com.giho.king_of_table_tennis.dto.ProfileRegistrationRequestDTO;
import com.giho.king_of_table_tennis.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Tag(name = "User-Controller", description = "사용자 관련 API")
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @Operation(summary = "nickName 중복 확인", description = "회원가입 시 사용하려는 nickName의 중복 여부를 확인하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "nickName 중복 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = CheckExistsResponseDTO.class)
    )
  )
  @GetMapping("/exists/nickName/{nickName}")
  public ResponseEntity<CheckExistsResponseDTO> checkNickNameDuplication(@PathVariable String nickName) {

    CheckExistsResponseDTO checkExistsResponse = userService.checkNickNameDuplication(nickName);
    return ResponseEntity.ok(checkExistsResponse);
  }

  @Operation(summary = "profileImage와 nickName 등록", description = "첫 로그인 시 profileImage와 nickName을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "profileImage와 nickName 변경 성공 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PatchMapping(value = "/profileImage/nickName", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<BooleanResponseDTO> profileRegistration(@ModelAttribute ProfileRegistrationRequestDTO profileRegistrationRequestDTO) {
    BooleanResponseDTO booleanResponse = userService.profileRegistration(profileRegistrationRequestDTO);
    return ResponseEntity.ok(booleanResponse);
  }
}

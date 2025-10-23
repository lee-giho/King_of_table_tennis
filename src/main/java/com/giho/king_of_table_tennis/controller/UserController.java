package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.service.PostService;
import com.giho.king_of_table_tennis.service.TokenService;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "User-Controller", description = "사용자 관련 API")
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  private final TokenService tokenService;

  private final PostService postService;

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

  @Operation(summary = "racketType과 userLevel 등록", description = "첫 로그인 시 racketType과 userLevel을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "racketType과 userLevel 등록 성공 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PostMapping("/tableTennisInfo")
  public ResponseEntity<BooleanResponseDTO> tableTennisInfoRegistration(@RequestBody TableTennisInfoRegistrationRequestDTO tableTennisInfoRegistrationRequestDTO) {
    BooleanResponseDTO booleanResponseDTO = userService.tableTennisInfoRegistration(tableTennisInfoRegistrationRequestDTO);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "accessToken 재발급", description = "refreshToken을 통해 accessToken을 재발급 받는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "새로 발급받은 accessToken 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = RefreshAccessTokenResponseDTO.class)
    )
  )
  @GetMapping("/accessToken")
  public ResponseEntity<RefreshAccessTokenResponseDTO> refreshAccessToken(@RequestHeader("Authorization") String refreshToken) {
    RefreshAccessTokenResponseDTO refreshAccessTokenResponseDTO = tokenService.refreshAccessTokenByRefreshToken(refreshToken);
    return ResponseEntity.ok(refreshAccessTokenResponseDTO);
  }

  @Operation(summary = "간단한 내 정보 가져오기", description = "마이페이지에서 보여줄 간단한 내 정보 반환하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "간단한 내 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = MySimpleInfoResponse.class)
    )
  )
  @GetMapping("/myInfo/simple")
  public ResponseEntity<MySimpleInfoResponse> getMySimpleInfo() {
    MySimpleInfoResponse myInfoResponse = userService.getMySimpleInfo();
    return ResponseEntity.ok(myInfoResponse);
  }

  @Operation(summary = "내 정보 가져오기", description = "마이페이지에서 보여줄 민감한 정보 제외한 내 정보 반환하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "내 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = UserInfo.class)
    )
  )
  @GetMapping("/myInfo")
  public ResponseEntity<UserInfo> getMyInfo() {
    UserInfo userInfo = userService.getMyInfo();
    return ResponseEntity.ok(userInfo);
  }

  @Operation(summary = "닉네임 변경하기", description = "기존의 닉네임을 새로운 닉네임으로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "닉네임 변경하기",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PatchMapping("/nickName")
  public ResponseEntity<BooleanResponseDTO> changeNickName(@RequestBody ChangeValueRequest changeValueRequest) {
    BooleanResponseDTO booleanResponseDTO = userService.changeNickName(changeValueRequest);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "라켓 타입 변경하기", description = "기존의 라켓 타입을 새로운 라켓 타입으로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "라켓 타입 변경하기",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PatchMapping("/racketType")
  public ResponseEntity<BooleanResponseDTO> changeRacketType(@RequestBody ChangeValueRequest changeValueRequest) {
    BooleanResponseDTO booleanResponseDTO = userService.changeRacketType(changeValueRequest);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "비밀번호 확인", description = "민감한 정보를 다루기 전 비밀번호 확인하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "비밀번호 확인",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PostMapping("/verification/password")
  public ResponseEntity<BooleanResponseDTO> verifyPassword(@RequestBody VerifyPasswordRequest verifyPasswordRequest) {
    BooleanResponseDTO booleanResponseDTO = userService.verifyPassword(verifyPasswordRequest);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "비밀번호 변경", description = "기존의 비밀번호를 새로운 비밀번호로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "비밀번호 변경",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PatchMapping("/password")
  public ResponseEntity<BooleanResponseDTO> changePassword(@RequestBody ChangeValueRequest changeValueRequest) {
    BooleanResponseDTO booleanResponseDTO = userService.changePassword(changeValueRequest);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "프로필 사진 변경", description = "기존의 프로필 사진을 새로운 사진으로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "프로필 사진 변경",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PatchMapping("/profileImage")
  public ResponseEntity<BooleanResponseDTO> uploadProfileImage(@ModelAttribute UploadProfileImageRequest uploadProfileImageRequest) {
    BooleanResponseDTO booleanResponseDTO = userService.changeProfileImage(uploadProfileImageRequest);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "프로필 사진을 기본으로 변경", description = "기존의 프로필 사진을 삭제하여 DB의 profileImage를 default로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "프로필 사진을 기본으로 변경",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @DeleteMapping("/profileImage/default")
  public ResponseEntity<BooleanResponseDTO> deleteProfileImage() {
    BooleanResponseDTO booleanResponseDTO = userService.deleteProfileImage();
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "사용자 정보 가져오기", description = "id를 통해 해당하는 사용자 정보 반환하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "사용자 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = UserInfo.class)
    )
  )
  @GetMapping("/info/{userId}")
  public ResponseEntity<UserInfo> getUserInfo(@PathVariable String userId) {
    UserInfo userInfo = userService.getUserInfo(userId);
    return ResponseEntity.ok(userInfo);
  }

  @Operation(summary = "내가 작성한 게시물 불러오기", description = "내가 작성한 게시물을 페이징을 통해 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "내가 작성한 게시물 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/me/posts")
  public ResponseEntity<PageResponse<PostDTO>> getMyPost(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size
  ) {

    PageResponse<PostDTO> pageResponse = postService.getPostByUser(page, size, null);
    return ResponseEntity.ok(pageResponse);
  }

  @Operation(summary = "특정 사용자가 작성한 게시물 불러오기", description = "특정 사용자가 작성한 게시물을 페이징을 통해 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "특정 사용자가 작성한 게시물 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/{userId}/posts")
  public ResponseEntity<PageResponse<PostDTO>> getUserPost(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size,
    @PathVariable String userId
  ) {

    PageResponse<PostDTO> pageResponse = postService.getPostByUser(page, size, userId);
    return ResponseEntity.ok(pageResponse);
  }
}
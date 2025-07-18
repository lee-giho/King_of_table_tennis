package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.ChangePasswordRequestDTO;
import com.giho.king_of_table_tennis.dto.CheckExistsResponseDTO;
import com.giho.king_of_table_tennis.dto.FindIdResponseDTO;
import com.giho.king_of_table_tennis.dto.RegisterRequestDTO;
import com.giho.king_of_table_tennis.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth-Controller", description = "회원가입, 아이디/닉네임 중복 체크 API")
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "회원가입", description = "회원가입 API")
  @ApiResponse(
    responseCode = "200",
    description = "회원가입 성공 여부 반환",
    content = @Content(
      mediaType = "text/plain",
      schema = @Schema(type = "string", example = "회원가입에 성공/실패했습니다.")
    )
  )
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterRequestDTO registerDTO) {

    boolean response = authService.register(registerDTO);

    if (response) {
      return ResponseEntity.ok("회원가입에 성공했습니다.");
    } else {
      return ResponseEntity.ok("회원가입에 실패했습니다.");
    }

  }

  @Operation(summary = "ID 중복 확인", description = "회원가입 시 사용하려는 ID의 중복 여부를 확인하는 API")
  @ApiResponse(
    responseCode = "200",
    description = "ID 중복 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = CheckExistsResponseDTO.class)
    )
  )
  @GetMapping("/exists/id/{id}")
  public ResponseEntity<CheckExistsResponseDTO> checkIdDuplication(@PathVariable String id) {

    CheckExistsResponseDTO checkExistsResponse = authService.checkIdDuplication(id);
    return ResponseEntity.ok(checkExistsResponse);
  }

  @Operation(summary = "id 찾기", description = "이름과 이메일을 통해 아이디를 찾고 반환해주는 API")
  @ApiResponse(
    responseCode = "200",
    description = "아이디 확인 후 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = FindIdResponseDTO.class)
    )
  )
  @GetMapping("/id")
  public ResponseEntity<FindIdResponseDTO> findId(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email) {
    FindIdResponseDTO findIdResponse = authService.findId(name, email);
    return ResponseEntity.ok(findIdResponse);
  }

  @Operation(summary = "비밀번호 찾기(변경)", description = "아이디와 이름, 이메일로 비밀번호 변경할 수 있는 API")
  @ApiResponse(
    responseCode = "200",
    description = "아이디와 이름, 이메일로 확인 후 비밀번호 변경",
    content = @Content(
      mediaType = "text/plain",
      schema = @Schema(type = "string", example = "비밀번호가 변경되었습니다.(변경되지 않았습니다.)")
    )
  )
  @PatchMapping("/password")
  public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequestDTO changePasswordRequest) {
    boolean response = authService.findPassword(changePasswordRequest);
    if (response) {
      return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    } else {
      return ResponseEntity.ok("비밀번호가 변경되지 않았습니다.");
    }
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.CheckExistsResponse;
import com.giho.king_of_table_tennis.dto.FindIdResponse;
import com.giho.king_of_table_tennis.dto.RegisterDTO;
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
@Tag(name = "Auth-Controller", description = "회원가입, 아이디/닉네임 중복 체크 API 엔드포인트")
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "회원가입", description = "회원가입 API")
  @ApiResponse(
    responseCode = "200",
    description = "회원가입 성공 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(type = "string", example = "회원가입에 성공/실패했습니다.")
    )
  )
  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {

    boolean response = authService.register(registerDTO);

    if (response) {
      return ResponseEntity.ok("회원가입에 성공했습니다.");
    } else {
      return ResponseEntity.ok("회원가입에 실패했습니다.");
    }

  }

  @Operation(summary = "ID 중복 확인", description = "회원가입 시 사용하려는 ID의 중복 여부를 확인합니다.")
  @ApiResponse(
    responseCode = "200",
    description = "ID 중복 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = CheckExistsResponse.class)
    )
  )
  @GetMapping("/exists/id/{id}")
  public ResponseEntity<CheckExistsResponse> checkIdDuplication(@PathVariable String id) {

    CheckExistsResponse checkExistsResponse = authService.checkIdDuplication(id);
    return ResponseEntity.ok(checkExistsResponse);
  }

  @Operation(summary = "nickName 중복 확인", description = "회원가입 시 사용하려는 nickName의 중복 여부를 확인합니다.")
  @ApiResponse(
    responseCode = "200",
    description = "nickName 중복 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = CheckExistsResponse.class)
    )
  )
  @GetMapping("/exists/nickName/{nickName}")
  public ResponseEntity<CheckExistsResponse> checkNickNameDuplication(@PathVariable String nickName) {

    CheckExistsResponse checkExistsResponse = authService.checkNickNameDuplication(nickName);
    return ResponseEntity.ok(checkExistsResponse);
  }

  @GetMapping("/id")
  public ResponseEntity<FindIdResponse> findId(@RequestParam(name = "name") String name, @RequestParam(name = "email") String email) {
    FindIdResponse findIdResponse = authService.findId(name, email);
    return ResponseEntity.ok(findIdResponse);
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.SendVerificationCodeResponse;
import com.giho.king_of_table_tennis.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Email-Controller", description = "인증번호 전송, 인증번호 확인 API 엔드포인트")
@RequestMapping("/api/auth/email")
public class EmailController {

  private final EmailService emailService;

  @Operation(
    summary = "인증번호 전송",
    description =
      "<h1>인증번호 전송 API</h1>" +
      "<h2>type</h2>" +
      "<ul>" +
        "<li>회원가입: register</li>" +
      "</ul>"
  )
  @ApiResponse(
    responseCode = "200",
    description = "인증번호가 저장되어 있는 세션 ID 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = SendVerificationCodeResponse.class)
    )
  )
  @GetMapping("/code/{type}/{email}")
  public ResponseEntity<SendVerificationCodeResponse> sendVerificationCode(@PathVariable String type, @PathVariable String email, HttpServletRequest request) {
    SendVerificationCodeResponse sendVerificationCodeResponse = emailService.sendVerificationEmail(type, email, request);
    return ResponseEntity.ok(sendVerificationCodeResponse);
  }

  @Operation(summary = "인증번호 확인", description = "인증번호 확인 API")
  @ApiResponse(
    responseCode = "200",
    description = "사용자가 입력한 인증번호와 세션에 저장된 인증번호를 확인 후 결과 반환",
    content = @Content(
      mediaType = "text/plain",
      schema = @Schema(type = "string", example = "인증번호가 일치합니다.(일치하지 않습니다.)")
    )
  )
  @GetMapping("/code/verify/{code}")
  public ResponseEntity<String> checkVerificationCode(@RequestHeader("sessionId") String sessionId, @PathVariable String code, HttpServletRequest request) {
    boolean response = emailService.checkVerificationCode(sessionId, code, request);
    if (response) {
      return ResponseEntity.ok("인증번호가 일치합니다.");
    } else {
      return ResponseEntity.ok("인증번호가 일치하지 않습니다.");
    }
  }
}

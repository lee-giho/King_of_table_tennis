package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.SendVerificationCodeResponse;
import com.giho.king_of_table_tennis.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/email")
public class EmailController {

  private final EmailService emailService;

  @GetMapping("/code/{type}/{email}")
  public ResponseEntity<SendVerificationCodeResponse> sendVerificationCode(@PathVariable String type, @PathVariable String email, HttpServletRequest request) {
    SendVerificationCodeResponse sendVerificationCodeResponse = emailService.sendVerificationEmail(type, email, request);
    return ResponseEntity.ok(sendVerificationCodeResponse);
  }

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

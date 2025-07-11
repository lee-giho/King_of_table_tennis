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
}

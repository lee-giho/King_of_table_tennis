package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.RegisterDTO;
import com.giho.king_of_table_tennis.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(RegisterDTO registerDTO) {

    boolean response = authService.register(registerDTO);

    if (response) {
      return ResponseEntity.ok("회원가입에 성공했습니다.");
    } else {
      return ResponseEntity.ok("회원가입에 실패했습니다.");
    }

  }
}

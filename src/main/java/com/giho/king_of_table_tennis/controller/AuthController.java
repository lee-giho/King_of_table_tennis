package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.CheckIdDuplicationDTO;
import com.giho.king_of_table_tennis.dto.RegisterDTO;
import com.giho.king_of_table_tennis.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {

    boolean response = authService.register(registerDTO);

    if (response) {
      return ResponseEntity.ok("회원가입에 성공했습니다.");
    } else {
      return ResponseEntity.ok("회원가입에 실패했습니다.");
    }

  }

  @GetMapping("/exists/id")
  public ResponseEntity<Boolean> checkIdDuplication(@RequestBody CheckIdDuplicationDTO checkIdDuplicationDTO) {

    boolean response = authService.checkIdDuplication(checkIdDuplicationDTO);
    return ResponseEntity.ok(response);
  }
}

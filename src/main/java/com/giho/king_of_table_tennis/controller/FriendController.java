package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.FriendRequestDTO;
import com.giho.king_of_table_tennis.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Friend-Controller", description = "친구 관련 API")
@RequestMapping("/api/friend")
public class FriendController {

  private final FriendService friendService;

  @Operation(summary = "친구 요청", description = "다른 사용자에게 친구 요청을 보내는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "201",
    description = "친구 요청 완료(본문 없음)"
  )
  @PostMapping()
  public ResponseEntity<Void> requestFriend(@RequestBody FriendRequestDTO friendRequestDTO) {
    friendService.requestFriend(friendRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

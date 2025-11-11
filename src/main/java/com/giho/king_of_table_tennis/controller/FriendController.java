package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.FriendRequestAnswerDTO;
import com.giho.king_of_table_tennis.dto.FriendRequestDTO;
import com.giho.king_of_table_tennis.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<Void> requestFriend(
    @RequestBody FriendRequestDTO friendRequestDTO
  ) {
    friendService.requestFriend(friendRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "친구 요청 응답", description = "자신이 받은 친구 요청을 수락/거절하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "친구 요청 응답 완료(본문 없음)"
  )
  @PatchMapping("/requests/{targetId}")
  public ResponseEntity<Void> responseFriendRequest(
    @PathVariable(name = "targetId") String targetId,
    @RequestBody FriendRequestAnswerDTO friendRequestAnswerDTO
  ) {
    friendService.responseFriendRequest(targetId, friendRequestAnswerDTO);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "친구 삭제", description = "서로 친구 상태인 사용자와의 관계를 삭제하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "친구 삭제 완료(본문 없음)"
  )
  @DeleteMapping("/{targetId}")
  public ResponseEntity<Void> deleteFriend(
    @PathVariable(name = "targetId") String targetId
  ) {
    friendService.deleteFriend(targetId);
    return ResponseEntity.noContent().build();
  }
}

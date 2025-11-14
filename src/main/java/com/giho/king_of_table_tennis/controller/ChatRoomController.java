package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.CreateChatRoomRequest;
import com.giho.king_of_table_tennis.dto.CreateChatRoomResponse;
import com.giho.king_of_table_tennis.service.ChatRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "ChatRoom-Controller", description = "채팅방 관련 API")
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {

  private final ChatRoomService chatRoomService;

  @Operation(summary = "1:1 채팅방 생성 및 조회", description = "상대방과 1:1 채팅방이 존재하면 해당 방의 ID를 응답하고, 없으면 새로 생성해 ID를 응답하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "채팅방 아이디 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = CreateChatRoomResponse.class)
    )
  )
  @PostMapping()
  public ResponseEntity<CreateChatRoomResponse> createOrGetChatRoom(
    @RequestBody CreateChatRoomRequest createChatRoomRequest
  ) {
    CreateChatRoomResponse createChatRoomResponse = chatRoomService.createOrGetChatRoom(createChatRoomRequest);
    return ResponseEntity.ok(createChatRoomResponse);
  }
}

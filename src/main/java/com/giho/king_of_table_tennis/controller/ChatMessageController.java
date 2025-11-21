package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.ChatMessage;
import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.service.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "ChatMessage-Controller", description = "채팅 메시지 관련 API")
@RequestMapping("/api/chat")
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

  @Operation(summary = "메시지 조회", description = "채팅방에 해당하는 메시지를 페이징으로 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "메시지 목록 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/rooms/{roomId}/messages")
  public ResponseEntity<PageResponse<ChatMessage>> getMessages(
    @PathVariable String roomId,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "20") int size
  ) {
    PageResponse<ChatMessage> pageResponse = chatMessageService.getMessages(roomId, page, size);
    return ResponseEntity.ok(pageResponse);
  }
}

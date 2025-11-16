package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.service.ChatMessageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "ChatMessage-Controller", description = "채팅 메시지 관련 API")
@RequestMapping("/api/chat")
public class ChatMessageController {

  private final ChatMessageService chatMessageService;

}

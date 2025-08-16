package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.BroadcastRoomInfo;
import com.giho.king_of_table_tennis.dto.CreateBroadcastRoomRequest;
import com.giho.king_of_table_tennis.service.BroadcastService;
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
@Tag(name = "Broadcast-Controller", description = "방송 관련 API")
@RequestMapping("/api/broadcast")
public class BroadcastController {

  private final BroadcastService broadcastService;

  @Operation(summary = "경기 방송 시작", description = "경기 방송을 위한 방 만드는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "Redis에 저장한 경기 방송 방 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BroadcastRoomInfo.class)
    )
  )
  @PostMapping("")
  public ResponseEntity<BroadcastRoomInfo> startBroadcast(@RequestBody CreateBroadcastRoomRequest createBroadcastRoomRequest) {
    BroadcastRoomInfo broadcastRoomInfo = broadcastService.saveBroadcastRoom(createBroadcastRoomRequest);
    return ResponseEntity.ok(broadcastRoomInfo);
  }

  @Operation(summary = "경기 방송 입장", description = "경기 방송을 입장하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "Redis에 저장되어 있는 경기 방송 방(방송 중인) 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BroadcastRoomInfo.class)
    )
  )
  @GetMapping("/enter/{gameInfoId}")
  public ResponseEntity<BroadcastRoomInfo> enterBroadcast(@PathVariable String gameInfoId) {
    BroadcastRoomInfo broadcastRoomInfo = broadcastService.enterBroadcast(gameInfoId);
    return ResponseEntity.ok(broadcastRoomInfo);
  }
}

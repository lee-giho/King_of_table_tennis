package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.CreateGameRequestDTO;
import com.giho.king_of_table_tennis.service.GameService;
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
@Tag(name = "Game-Controller", description = "게임 관련 API")
@RequestMapping("/api/game")
public class GameController {

  private final GameService gameService;

  @Operation(summary = "게임 생성", description = "게임 생성을 위해 입력한 정보를 저장하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "GameInfo와 GameState 저장 성공 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PostMapping("")
  public ResponseEntity<BooleanResponseDTO> createGame(@RequestBody CreateGameRequestDTO createGameRequestDTO) {
    BooleanResponseDTO booleanResponseDTO = gameService.createGame(createGameRequestDTO);
    return ResponseEntity.ok(booleanResponseDTO);
  }
}

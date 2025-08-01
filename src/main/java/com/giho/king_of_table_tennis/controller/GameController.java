package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.CreateGameRequestDTO;
import com.giho.king_of_table_tennis.dto.GameParticipationRequestDTO;
import com.giho.king_of_table_tennis.dto.SelectedGameDateResponseDTO;
import com.giho.king_of_table_tennis.service.GameService;
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

  @Operation(summary = "선택 불가능한 날짜 가져오기", description = "게임 생성을 할 때 선택 불가능한 날짜를 가져오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "선택 불가능한 날짜 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = SelectedGameDateResponseDTO.class)
    )
  )
  @GetMapping("/selectedGameDate/{tableTennisCourtId}")
  public ResponseEntity<SelectedGameDateResponseDTO> getSelectedGameDate(@PathVariable String tableTennisCourtId) {
    SelectedGameDateResponseDTO selectedGameDateResponseDTO = gameService.getSelectedGameDate(tableTennisCourtId);
    return ResponseEntity.ok(selectedGameDateResponseDTO);
  }

  @Operation(summary = "탁구 경기 참가 신청", description = "수락 타입에 따라 탁구 경기 참가 신청을 하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "탁구 경기 참가 신청 완료 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @PostMapping("/participation")
  public ResponseEntity<BooleanResponseDTO> gameParticipation(@RequestBody GameParticipationRequestDTO gameParticipationRequestDTO) {
    BooleanResponseDTO booleanResponseDTO = gameService.gameParticipation(gameParticipationRequestDTO);
    return ResponseEntity.ok(booleanResponseDTO);
  }
}

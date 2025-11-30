package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

  @Operation(summary = "랜덤 제목 가져오기", description = "게임 생성을 할 때 랜덤 경기 제목을 가져오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "랜덤 경기 제목 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = RandomGameTitleResponse.class)
    )
  )
  @GetMapping("/title/random")
  public ResponseEntity<RandomGameTitleResponse> getRandomTitle() {
    RandomGameTitleResponse randomGameTitleResponse = gameService.getRandomTitle();
    return ResponseEntity.ok(randomGameTitleResponse);
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

  @Operation(summary = "등록된 경기 불러오기", description = "탁구장 별로 등록되어 있는 경기 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "탁구장에 등록되어 있는 경기 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = RecruitingGameListDTO.class)
    )
  )
  @GetMapping("/recruitingList/{tableTennisCourtId}/{type}")
  public ResponseEntity<PageResponse<RecruitingGameDTO>> getRecruitingGameList(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @PathVariable String tableTennisCourtId,
    @PathVariable String type
  ) {

    PageResponse<RecruitingGameDTO> pageResponse = gameService.getRecruitingGameList(tableTennisCourtId, type, page, size);
    return ResponseEntity.ok(pageResponse);
  }

  @Operation(summary = "경기 종료 및 결과 저장", description = "경기 상태와 세트 점수, 승/패, 랭킹을 업데이트 하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "경기 종료 및 결과 저장 성공(본문 없음)"
  )
  @PostMapping("/{gameInfoId}/result")
  public ResponseEntity<Void> finishGame(
    @PathVariable(name = "gameInfoId") String gameInfoId,
    @RequestBody FinishGameRequest finishGameRequest
  ) {
    gameService.finishGame(gameInfoId, finishGameRequest);
    return ResponseEntity.noContent().build();
  }


  @Operation(summary = "경기에 대한 자세한 정보 불러오기", description = "참가자 정보와 경기 정보 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "경기에 대한 참가자와 경기 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = GameDetailInfo.class)
    )
  )
  @GetMapping("/detailInfo/{gameInfoId}")
  public ResponseEntity<GameDetailInfo> getGameDetailInfo(@PathVariable String gameInfoId) {
    GameDetailInfo gameDetailInfo = gameService.getGameDetailInfo(gameInfoId);
    return ResponseEntity.ok(gameDetailInfo);
  }

  @Operation(summary = "페이징을 통해 경기에 대한 자세한 정보 한 개씩 불러오기", description = "참가자 정보와 경기 정보 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "페이징을 통한 경기에 대한 참가자와 경기 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/detailInfo/latest/{place}")
  public ResponseEntity<PageResponse<GameDetailInfoByPage>> getGameDetailInfoByPage(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "1") int size,
    @PathVariable String place) {

    Pageable pageable = PageRequest.of(page, size);
    Page<GameDetailInfoByPage> gameDetailInfoPage = gameService.getGameDetailInfoByPage(pageable, place);

    PageResponse<GameDetailInfoByPage> body = new PageResponse<>(
      gameDetailInfoPage.getContent(),
      gameDetailInfoPage.getTotalPages(),
      gameDetailInfoPage.getTotalElements(),
      gameDetailInfoPage.getNumber(),
      gameDetailInfoPage.getSize()
    );

    return ResponseEntity.ok(body);
  }

  @Operation(summary = "페이징을 통해 경기에 대한 자세한 정보 불러오기 / 마이페이지의 탁구 경기 내역", description = "사용자 아이디와 경기 전/후로 경기 정보 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "페이징을 통한 경기에 대한 참가자와 경기 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/detailInfo/myGame/{type}")
  public ResponseEntity<PageResponse<GameDetailInfoByUser>> getGameDetailInfoByUser(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @PathVariable String type) {

    Pageable pageable = PageRequest.of(page, size);
    Page<GameDetailInfoByUser> gameDetailInfoByUser = gameService.getGameDetailInfoByUser(type, pageable);

    PageResponse<GameDetailInfoByUser> body = new PageResponse<>(
      gameDetailInfoByUser.getContent(),
      gameDetailInfoByUser.getTotalPages(),
      gameDetailInfoByUser.getTotalElements(),
      gameDetailInfoByUser.getNumber(),
      gameDetailInfoByUser.getSize()
    );

    return ResponseEntity.ok(body);
  }

  @Operation(summary = "탁구 경기 참가 취소", description = "신청했던 탁구 경기를 취소하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "탁구 경기 참가 신청 취소 완료 여부 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = BooleanResponseDTO.class)
    )
  )
  @DeleteMapping("/participation/{gameInfoId}")
  public ResponseEntity<BooleanResponseDTO> cancelGameParticipation(@PathVariable String gameInfoId) {
    BooleanResponseDTO booleanResponseDTO = gameService.deleteGameParticipation(gameInfoId);
    return ResponseEntity.ok(booleanResponseDTO);
  }

  @Operation(summary = "탁구 경기 참가자 목록", description = "해당 경기에 신청한 참가자들의 목록을 페이징을 통해 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "페이징을 통한 탁구 경기 참가자 정보 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/{gameInfoId}/applicant")
  public ResponseEntity<PageResponse<UserInfo>> getApplicantInfo(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @PathVariable String gameInfoId) {

    Pageable pageable = PageRequest.of(page, size);
    Page<UserInfo> userInfoPage = gameService.getApplicantInfo(pageable, gameInfoId);

    PageResponse<UserInfo> body = new PageResponse<>(
      userInfoPage.getContent(),
      userInfoPage.getTotalPages(),
      userInfoPage.getTotalElements(),
      userInfoPage.getNumber(),
      userInfoPage.getSize()
    );

    return ResponseEntity.ok(body);
  }

  @Operation(summary = "경기 신청자 수락", description = "특정 경기의 신청자 한 명을 수락하고, 해당 경기의 모든 신청 레코드를 삭제한 뒤 경기 상태를 WAITING으로 변경하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "신청자 수락 완료(본문 없음)"
  )
  @PatchMapping("/{gameInfoId}/applications/{applicantId}/acceptance")
  public ResponseEntity<Void> acceptApplicant(
    @PathVariable String gameInfoId,
    @PathVariable String applicantId) {

    gameService.acceptApplicant(gameInfoId, applicantId);
    return ResponseEntity.noContent().build(); // 204
  }

  @Operation(summary = "경기 취소", description = "시작 전(RECRUITING, WAITING)인 경기를 취소하는 API / 관련 데이터도 함께 삭제", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "경기 취소 완료(본문 없음)"
  )
  @DeleteMapping("/{gameInfoId}")
  public ResponseEntity<Void> deleteGame(
    @PathVariable String gameInfoId
  ) {

    gameService.deleteGame(gameInfoId);
    return ResponseEntity.noContent().build();
  }
}
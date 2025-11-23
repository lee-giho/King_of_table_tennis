package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.UserRankingInfo;
import com.giho.king_of_table_tennis.dto.enums.RankingSortOption;
import com.giho.king_of_table_tennis.service.UserRankingService;
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
@Tag(name = "UserRanking-Controller", description = "사용자 랭킹 관련 API")
@RequestMapping("/api/users")
public class UserRankingController {

  private final UserRankingService userRankingService;

  @Operation(summary = "랭킹 조회", description = "사용자들의 랭킹을 조회하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "랭킹 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/rankings")
  public ResponseEntity<PageResponse<UserRankingInfo>> getUserRankings(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "20") int size,
    @RequestParam(name = "sort", defaultValue = "WIN_RATE") RankingSortOption sort
  ) {

    PageResponse<UserRankingInfo> pageResponse = userRankingService.getUserRankings(page, size, sort);
    return ResponseEntity.ok(pageResponse);
  }

  @GetMapping("/{userId}/ranking")
  public ResponseEntity<UserRankingInfo> getUserRanking(
    @PathVariable(name = "userId") String userId,
    @RequestParam(name = "sort", defaultValue = "WIN_RATE") RankingSortOption sort
  ) {

    UserRankingInfo userRankingInfo = userRankingService.getUserRanking(userId, sort);
    return ResponseEntity.ok(userRankingInfo);
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.GameReviewDTO;
import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.RegisterReviewRequestDTO;
import com.giho.king_of_table_tennis.dto.UpdateGameReviewRequestDTO;
import com.giho.king_of_table_tennis.service.ReviewService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Review-Controller", description = "리뷰 관련 API")
@RequestMapping("/api/review")
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "경기 리뷰 작성", description = "경기 종료 후 작성한 리뷰 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "201",
    description = "경기 리뷰 작성 완료(본문 없음)"
  )
  @PostMapping("/{gameInfoId}")
  public ResponseEntity<Void> registerReview(
    @PathVariable String gameInfoId,
    @RequestBody RegisterReviewRequestDTO registerReviewRequestDTO
  ) {
    reviewService.registerReview(gameInfoId, registerReviewRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "페이징을 통해 리뷰 불러오기 / 마이페이지의 탁구 리뷰 내역", description = "사용자 아이디와 내가 작성한 것인지에 대한 정보(type)로 경기 리뷰 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "페이징을 통해 경기 리뷰 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/{type}")
  public ResponseEntity<PageResponse<GameReviewDTO>> getGameReview(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "5") int size,
    @PathVariable String type
  ) {

    Pageable pageable = PageRequest.of(page, size);
    Page<GameReviewDTO> gameReviewDTOPage = reviewService.getGameReview(type, pageable);

    PageResponse<GameReviewDTO> body = new PageResponse<>(
      gameReviewDTOPage.getContent(),
      gameReviewDTOPage.getTotalPages(),
      gameReviewDTOPage.getTotalElements(),
      gameReviewDTOPage.getNumber(),
      gameReviewDTOPage.getSize()
    );

    return ResponseEntity.ok(body);
  }

  @Operation(summary = "경기 리뷰 수정", description = "경기 종료 후 작성했던 리뷰 수정하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "경기 리뷰 수정 완료(본문 없음)"
  )
  @PatchMapping("/{gameReviewId}")
  public ResponseEntity<Void> patchReview(
    @PathVariable String gameReviewId,
    @RequestBody UpdateGameReviewRequestDTO updateGameReviewRequestDTO
    ) {

    reviewService.updateGameReview(gameReviewId, updateGameReviewRequestDTO);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "경기 리뷰 삭제", description = "경기 종료 후 작성했던 리뷰 삭제하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "경기 리뷰 삭제 완료(본문 없음)"
  )
  @DeleteMapping("/{gameReviewId}")
  public ResponseEntity<Void> deleteReview(
    @PathVariable String gameReviewId
  ) {

    reviewService.deleteReview(gameReviewId);
    return ResponseEntity.noContent().build();
  }
}

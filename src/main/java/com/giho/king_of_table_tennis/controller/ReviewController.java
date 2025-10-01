package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.RegisterReviewRequestDTO;
import com.giho.king_of_table_tennis.service.ReviewService;
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
}

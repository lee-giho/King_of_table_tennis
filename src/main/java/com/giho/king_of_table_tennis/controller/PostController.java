package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.RegisterPostRequestDTO;
import com.giho.king_of_table_tennis.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post-Controller", description = "게시물 관련 API")
@RequestMapping("/api/post")
public class PostController {

  private final PostService postService;

  @Operation(summary = "게시글 작성", description = "작성한 게시물을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "201",
    description = "게시글 작성 완료(본문 없음)"
  )
  @PostMapping()
  public ResponseEntity<Void> registerPost(
    @RequestBody RegisterPostRequestDTO registerPostRequestDTO
  ) {
    postService.registerPost(registerPostRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}

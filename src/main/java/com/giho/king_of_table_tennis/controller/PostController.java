package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.PageResponse;
import com.giho.king_of_table_tennis.dto.PostDTO;
import com.giho.king_of_table_tennis.dto.RegisterPostRequestDTO;
import com.giho.king_of_table_tennis.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @Operation(summary = "내가 작성한 게시물 불러오기", description = "내가 작성한 게시물을 페이징을 통해 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "내가 작성한 게시물 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/mine")
  public ResponseEntity<PageResponse<PostDTO>> getMyPost(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size
  ) {

    PageResponse<PostDTO> pageResponse = postService.getPost(page, size, null);
    return ResponseEntity.ok(pageResponse);
  }

  @Operation(summary = "특정 사용자가 작성한 게시물 불러오기", description = "특정 사용자가 작성한 게시물을 페이징을 통해 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "특정 사용자가 작성한 게시물 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/{userId}")
  public ResponseEntity<PageResponse<PostDTO>> getUserPost(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size,
    @PathVariable String userId
  ) {

    PageResponse<PostDTO> pageResponse = postService.getPost(page, size, userId);
    return ResponseEntity.ok(pageResponse);
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.RegisterPostRequestDTO;
import com.giho.king_of_table_tennis.dto.UpdatePostRequestDTO;
import com.giho.king_of_table_tennis.service.PostService;
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

  @Operation(summary = "게시글 삭제", description = "자신이 작성한 게시글 삭제하는는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "게시글 삭제 완료(본문 없음)"
  )
  @DeleteMapping("/{postId}")
  public ResponseEntity<Void> deletePost(
    @PathVariable String postId
  ) {

    postService.deletePost(postId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "게시글 수정", description = "자신이 작성했던 게시글 수정하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "게시글 수정 완료(본문 없음)"
  )
  @PatchMapping("/{postId}")
  public ResponseEntity<Void> patchPost(
    @PathVariable String postId,
    @RequestBody UpdatePostRequestDTO updatePostRequestDTO
  ) {

    postService.updatePost(postId, updatePostRequestDTO);
    return ResponseEntity.noContent().build();
  }
}

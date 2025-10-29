package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.RegisterCommentRequestDTO;
import com.giho.king_of_table_tennis.service.CommentService;
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
@Tag(name = "Comment-Controller", description = "댓글 관련 API")
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 작성", description = "게시물에 작성한 댓글을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "201",
    description = "댓글 작성 완료(본문 없음)"
  )
  @PostMapping("/{postId}")
  public ResponseEntity<Void> registerComment(
    @RequestBody RegisterCommentRequestDTO registerCommentRequestDTO,
    @PathVariable String postId
  ) {
    commentService.registerComment(postId, registerCommentRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}

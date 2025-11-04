package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.UpdateCommentRequestDTO;
import com.giho.king_of_table_tennis.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment-Controller", description = "댓글 관련 API")
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;

  @Operation(summary = "댓글 삭제", description = "자신이 작성한 댓글 삭제하는는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "댓글 삭제 완료(본문 없음)"
  )
  @DeleteMapping("/{commentId}")
  public ResponseEntity<Void> deleteComment(
    @PathVariable String commentId
  ) {

    commentService.deleteComment(commentId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "댓글 수정", description = "자신이 작성했던 댓글 수정하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "204",
    description = "댓글 수정 완료(본문 없음)"
  )
  @PatchMapping("/{commentId}")
  public ResponseEntity<Void> patchComment(
    @PathVariable String commentId,
    @RequestBody UpdateCommentRequestDTO updateCommentRequestDTO
  ) {

    commentService.updateComment(commentId, updateCommentRequestDTO);
    return ResponseEntity.noContent().build();
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.dto.enums.CommentSortOption;
import com.giho.king_of_table_tennis.dto.enums.PostSortOption;
import com.giho.king_of_table_tennis.entity.PostCategory;
import com.giho.king_of_table_tennis.service.CommentService;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Post-Controller", description = "게시글 관련 API")
@RequestMapping("/api/post")
public class PostController {

  private final PostService postService;

  private final CommentService commentService;

  @Operation(summary = "게시글 작성", description = "작성한 게시글을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
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

  @Operation(summary = "id로 게시글 불러오기", description = "postId로 해당 게시글의 정보를 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "id에 해당하는 게시물 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PostDTO.class)
    )
  )
  @GetMapping("/{postId}")
  public ResponseEntity<PostDTO> getPost(
    @PathVariable String postId
  ) {

    PostDTO postDTO = postService.getPostByPostId(postId);
    return ResponseEntity.ok(postDTO);
  }

  @Operation(summary = "전체 게시글 불러오기", description = "카테고리(GENERAL, SKILL, EQUIPMENT)를 선택해 게시글 목록을 페이징으로 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "게시글 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping()
  public ResponseEntity<PageResponse<PostDTO>> getPostList(
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "10") int size,
    @RequestParam(name = "category", required = false) List<PostCategory> categories,
    @RequestParam(name = "sort", defaultValue = "CREATED_DESC") PostSortOption sort,
    @RequestParam(name = "keyword", required = false) String keyword
  ) {

    PageResponse<PostDTO> pageResponse = postService.getPostList(page, size, categories, sort, keyword);
    return ResponseEntity.ok(pageResponse);
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

  @Operation(summary = "댓글 작성", description = "게시물에 작성한 댓글을 등록하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "201",
    description = "댓글 작성 완료(본문 없음)"
  )
  @PostMapping("/{postId}/comment")
  public ResponseEntity<Void> registerComment(
    @RequestBody RegisterCommentRequestDTO registerCommentRequestDTO,
    @PathVariable String postId
  ) {
    commentService.registerComment(postId, registerCommentRequestDTO);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Operation(summary = "게시글에 작성된 댓글 조회", description = "게시글에 해당하는 댓글 목록을 페이징으로 불러오는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "댓글 목록 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = PageResponse.class)
    )
  )
  @GetMapping("/{postId}/comments")
  public ResponseEntity<PageResponse<CommentDTO>> getComments(
    @PathVariable String postId,
    @RequestParam(name = "page", defaultValue = "0") int page,
    @RequestParam(name = "size", defaultValue = "20") int size,
    @RequestParam(name = "sort", defaultValue = "CREATED_DESC") CommentSortOption sort,
    @RequestParam(name = "showMyComment", defaultValue = "false") boolean showMyComment
  ) {
    PageResponse<CommentDTO> pageResponse = commentService.getCommentList(postId, page, size, sort, showMyComment);
    return ResponseEntity.ok(pageResponse);
  }
}

package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.service.CommentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comment-Controller", description = "댓글 관련 API")
@RequestMapping("/api/comment")
public class CommentController {

  private final CommentService commentService;



}

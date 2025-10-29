package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "자신이 작성한 게시글 수정 요청 DTO")
public class UpdatePostRequestDTO {
  private String title;
  private PostCategory category;
  private String content;
}

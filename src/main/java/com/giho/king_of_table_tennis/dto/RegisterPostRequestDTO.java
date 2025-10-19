package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "게시글 등록 요청 DTO")
public class RegisterPostRequestDTO {
  private String title;
  @Schema(allowableValues = {"GENERAL", "SKILL", "EQUIPMENT"})
  private String category;
  private String content;
}

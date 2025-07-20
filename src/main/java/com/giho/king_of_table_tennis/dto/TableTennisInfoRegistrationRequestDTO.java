package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "racketType과 level 등록 요청 DTO")
public class TableTennisInfoRegistrationRequestDTO {
  private String racketType;
  private String userLevel;
}

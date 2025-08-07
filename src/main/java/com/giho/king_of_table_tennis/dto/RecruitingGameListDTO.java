package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "경기 정보 리스트 DTO")
public class RecruitingGameListDTO {
  private List<RecruitingGameDTO> recruitingGames;
}

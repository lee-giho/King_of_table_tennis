package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.TableTennisCourtEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "탁구장 정보 리스트 응답 DTO")
public class TableTennisCourtResponseDTO {
  private List<TableTennisCourtEntity> tableTennisCourts;
}

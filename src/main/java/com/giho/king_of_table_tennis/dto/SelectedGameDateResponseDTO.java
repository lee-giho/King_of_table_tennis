package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "이미 선택된 탁구 경기 날짜 DTO")
public class SelectedGameDateResponseDTO {
  private List<LocalDateTime> selectedGameDateList;
}

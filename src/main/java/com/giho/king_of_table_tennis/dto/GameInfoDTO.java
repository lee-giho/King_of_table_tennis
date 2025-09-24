package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.AcceptanceType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "place를 id가 아닌 name을 사용하는 DTO")
public class GameInfoDTO {
  private String id;
  private int gameSet;
  private int gameScore;
  private String place;
  private AcceptanceType acceptanceType;
  private LocalDateTime gameDate;
}
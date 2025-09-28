package com.giho.king_of_table_tennis.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Schema(description = "에러 응답")
public class ErrorResponse {
  @Schema(description = "에러 코드")
  private final String code;

  @Schema(description = "에러 메시지")
  private final String message;

  @Schema(description = "발생 시각")
  private final LocalDateTime timestamp;

  @Schema(description = "요청 경로")
  private final String path;

}

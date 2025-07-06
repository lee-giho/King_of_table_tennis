package com.giho.king_of_table_tennis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {

  private final String code;
  private final String message;
  private final LocalDateTime timestamp;
  private final String path;

}

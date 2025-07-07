package com.giho.king_of_table_tennis.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // CustomException 처리
  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException e, HttpServletRequest request) {
    ErrorCode errorCode = e.getErrorCode();
    ErrorResponse response = new ErrorResponse(
      errorCode.name(),
      errorCode.getMessage(),
      LocalDateTime.now(),
      request.getRequestURI()
    );
    return ResponseEntity.status(errorCode.getStatus()).body(response);
  }

  //

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnhandledException(Exception e, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse(
      ErrorCode.INTERNAL_SERVER_ERROR.name(),
      e.getMessage(),
      LocalDateTime.now(),
      request.getRequestURI()
    );
    return ResponseEntity.status(500).body(response);
  }

}

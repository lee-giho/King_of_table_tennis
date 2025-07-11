package com.giho.king_of_table_tennis.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

  // 사용자
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 찾을 수 없습니다."),
  USER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 존재하는 사용자입니다."),

  // 인증번호 / 이메일
  VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
  INVALID_SESSION(HttpStatus.BAD_REQUEST, "세션이 유효하지 않습니다."),
  EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증번호 전송 중 오류가 발생했습니다."),

  // 일반
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;
}

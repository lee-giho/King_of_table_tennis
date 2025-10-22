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

  // 탁구 정보
  TABLE_TENNIS_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자에 대한 탁구 정보를 찾을 수 없습니다."),
  TABLE_TENNIS_INFO_ALREADY_EXIST(HttpStatus.CONFLICT, "해당 사용자에 대한 탁구 정보가 이미 존재합니다."),

  // 탁구 경기
  GAME_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "탁구 경기를 찾을 수 없습니다."),
  GAME_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "탁구 상태를 찾을 수 없습니다."),
  GAME_NOT_RECRUITING(HttpStatus.NOT_FOUND, "상대방 모집중이 아닙니다."),
  GAME_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 경기를 삭제할 권한이 없습니다."),
  GAME_NOT_DELETABLE(HttpStatus.CONFLICT, "해당 상태의 경기는 삭제할 수 없습니다."),

  // 탁구 경기 신청
  GAME_APPLICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "탁구 경기 신청 정보를 찾을 수 없습니다."),
  CHALLENGER_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 탁구 경기의 상대방이 존재합니다."),

  // 탁구 경기 리뷰
  GAME_REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "탁구 경기 리뷰를 찾을 수 없습니다."),
  REVIEW_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 작성한 리뷰가 존재합니다."),
  SELF_REVIEW_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "자기 자신에게 리뷰를 작성할 수 없습니다."),
  INVALID_REVIEWEE(HttpStatus.BAD_REQUEST, "해당 경기에 속하지 않은 사용자입니다."),
  GAME_NOT_ENDED(HttpStatus.CONFLICT, "탁구 경기가 아직 종료되지 않았습니다."),
  GAME_REVIEW_EDIT_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 리뷰를 수정할 권한이 없습니다."),
  GAME_REVIEW_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 리뷰를 삭제할 권한이 없습니다."),

  // 게시글
  POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 게시글을 찾을 수 없습니다."),
  POST_DELETE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 게시물을 삭제할 권한이 없습니다."),
  POST_UPDATE_FORBIDDEN(HttpStatus.FORBIDDEN, "해당 게시물을 수정할 권한이 없습니다."),
  INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "게시글 카테고리가 유효하지 않습니다."),

  // 인증번호 / 이메일
  VERIFICATION_CODE_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
  INVALID_SESSION(HttpStatus.BAD_REQUEST, "세션이 유효하지 않습니다."),
  EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "인증번호 전송 중 오류가 발생했습니다."),

  // 파일 처리
  FILE_STORAGE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 저장 중 오류가 발생했습니다."),
  FILE_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 삭제 중 오류가 발생했습니다."),
  DIR_CREATE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "폴더 생성 중 오류가 발생했습니다."),

  // 데이터베이스 처리
  DB_SAVE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 저장 중 오류가 발생했습니다."),
  DB_DELETE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "데이터베이스 삭제 중 오류가 발생했습니다."),

  // 인증/인가 관련
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),

  // JSON
  JSON_PARSE_FAILED(HttpStatus.BAD_REQUEST, "JSON 파싱 실패"),
  JSON_SERIALIZE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 직렬화 실패"),

  // 라이브 방
  BROADCAST_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "라이브 방을 찾을 수 없습니다."),
  BROADCAST_PLAYER_NOT_FOUND(HttpStatus.NOT_FOUND, "라이브에서 해당 사용자를 찾을 수 없습니다."),

  // 일반
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;
}

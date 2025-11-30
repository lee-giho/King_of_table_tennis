package com.giho.king_of_table_tennis.enums;

public enum GameTitleTemplate {
  MATCH_REQUEST("%s님의 매치 요청"),
  OPEN_CHALLENGE("%s님의 오픈 챌린지"),
  COMPETITIVE_RALLY("%s님의 경쟁 랠리"),
  PRACTICE_GAME("%s님의 연습 경기"),
  LOOKING_FOR_OPPONENT("%s님이 상대를 찾고 있습니다."),
  READY_FOR_MATCH("%s님이 경기를 준비 중입니다."),
  SEEKING_CHALLENGER("%s님, 도전자를 기다립니다."),
  TRAINING_MATCH("%s님의 트레이닝 매치");

  private final String pattern;

  GameTitleTemplate(String pattern) {
    this.pattern = pattern;
  }

  public String format(String nickName) {
    return String.format(pattern, nickName);
  }
}

package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "game_review")
public class GameReviewEntity {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "reviewer_id", nullable = false)
  private String reviewerId;

  @Column(name = "reviewee_id", nullable = false)
  private String revieweeId;

  @Column(name = "game_info_id", nullable = false)
  private String gameInfoId;

  // 기술 관련 점수
  @Column(name = "score_serve", nullable = false)
  private int scoreServe;

  @Column(name = "score_receive", nullable = false)
  private int scoreReceive;

  @Column(name = "score_rally", nullable = false)
  private int scoreRally;

  @Column(name = "score_strokes", nullable = false)
  private int scoreStrokes;

  @Column(name = "score_strategy", nullable = false)
  private int scoreStrategy;

  // 매너 관련 점수
  @Column(name = "score_manner", nullable = false)
  private int scoreManner;

  @Column(name = "score_punctuality", nullable = false)
  private int scorePunctuality;

  @Column(name = "score_community", nullable = false)
  private int scoreCommunity;

  @Column(name = "score_politeness", nullable = false)
  private int scorePoliteness;

  @Column(name = "score_rematch", nullable = false)
  private int scoreRematch;

  // 추가 코멘트
  @Column(name = "comment", nullable = true, length = 250)
  private String comment;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", updatable = false, insertable = false)
  private LocalDateTime updatedAt;
}

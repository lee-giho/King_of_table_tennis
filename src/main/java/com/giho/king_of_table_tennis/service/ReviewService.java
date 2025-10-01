package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.RegisterReviewRequestDTO;
import com.giho.king_of_table_tennis.entity.GameReviewEntity;
import com.giho.king_of_table_tennis.entity.GameState;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.GameReviewRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final GameReviewRepository gameReviewRepository;

  private final GameStateRepository gameStateRepository;

  @Transactional
  public void registerReview(String gameInfoId, RegisterReviewRequestDTO registerReviewRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    // 중복 리뷰 방지
    boolean exists = gameReviewRepository.existsByGameInfoIdAndReviewerIdAndRevieweeId(gameInfoId, userId, registerReviewRequestDTO.getRevieweeId());
    if (exists) {
      throw new CustomException(ErrorCode.REVIEW_ALREADY_EXIST);
    }

    // 자기 자신 리뷰 방지
    if (userId.equals(registerReviewRequestDTO.getRevieweeId())) {
      throw new CustomException(ErrorCode.SELF_REVIEW_NOT_ALLOWED);
    }

    // 경기 검증
    GameStateEntity gameStateEntity = gameStateRepository.findByGameInfoId(gameInfoId)
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));
    if (!(gameStateEntity.getDefenderId().equals(registerReviewRequestDTO.getRevieweeId()) ||
      gameStateEntity.getChallengerId().equals(registerReviewRequestDTO.getRevieweeId()))) {
      throw new CustomException(ErrorCode.INVALID_REVIEWEE);
    }
    if (!gameStateEntity.getState().equals(GameState.END)) {
      throw new CustomException(ErrorCode.GAME_NOT_ENDED);
    }

    GameReviewEntity gameReviewEntity = new GameReviewEntity();
    gameReviewEntity.setId(UUID.randomUUID().toString());
    gameReviewEntity.setReviewerId(userId);
    gameReviewEntity.setRevieweeId(registerReviewRequestDTO.getRevieweeId());
    gameReviewEntity.setGameInfoId(gameInfoId);

    gameReviewEntity.setScoreServe(registerReviewRequestDTO.getScoreServe());
    gameReviewEntity.setScoreReceive(registerReviewRequestDTO.getScoreReceive());
    gameReviewEntity.setScoreRally(registerReviewRequestDTO.getScoreRally());
    gameReviewEntity.setScoreStrokes(registerReviewRequestDTO.getScoreStrokes());
    gameReviewEntity.setScoreStrategy(registerReviewRequestDTO.getScoreStrategy());

    gameReviewEntity.setScoreManner(registerReviewRequestDTO.getScoreManner());
    gameReviewEntity.setScorePunctuality(registerReviewRequestDTO.getScorePunctuality());
    gameReviewEntity.setScoreCommunity(registerReviewRequestDTO.getScoreCommunity());
    gameReviewEntity.setScorePoliteness(registerReviewRequestDTO.getScorePoliteness());
    gameReviewEntity.setScoreRematch(registerReviewRequestDTO.getScoreRematch());

    gameReviewEntity.setComment(registerReviewRequestDTO.getComment());

    gameReviewRepository.save(gameReviewEntity);
  }
}

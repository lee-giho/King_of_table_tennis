package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.*;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.GameReviewRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import com.giho.king_of_table_tennis.repository.TableTennisCourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

  private final GameReviewRepository gameReviewRepository;

  private final GameStateRepository gameStateRepository;

  private final TableTennisCourtRepository tableTennisCourtRepository;

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

  public Page<GameReviewDTO> getGameReview(String type, Pageable pageable) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    Page<Object[]> rawPage;
    if (Objects.equals(type, ReviewType.RECEIVED.toString())) {
      rawPage = gameReviewRepository.findAllReceivedReviewDetails(userId, userId, pageable);
    } else {
      rawPage = gameReviewRepository.findAllWrittenReviewDetails(userId, userId, pageable);
    }

    if (rawPage.isEmpty()) {
      return Page.empty(pageable);
    }

    List<String> placeIds = rawPage.getContent().stream()
      .map(row -> ((GameInfoEntity) row[1]).getPlace())
      .distinct()
      .toList();

    Map<String, String> placeNameById = tableTennisCourtRepository.findAllById(placeIds)
      .stream()
      .collect(Collectors.toMap(TableTennisCourtEntity::getId, TableTennisCourtEntity::getName));

    List<GameReviewDTO> content = rawPage.getContent().stream().map(row -> {
      GameReviewEntity gr = (GameReviewEntity) row[0];
      GameInfoEntity g = (GameInfoEntity) row[1];
      UserInfo userInfo = getUserInfo(row);

      String placeName = placeNameById.getOrDefault(g.getPlace(), g.getPlace());

      GameInfoDTO gameInfoDTO = new GameInfoDTO(
        g.getId(),
        g.getTitle(),
        g.getGameSet(),
        g.getGameScore(),
        placeName,
        g.getAcceptanceType(),
        g.getGameDate()
      );

      return new GameReviewDTO(
        gr.getId(),
        userInfo,
        gameInfoDTO,
        gr.getScoreServe(),
        gr.getScoreReceive(),
        gr.getScoreRally(),
        gr.getScoreStrokes(),
        gr.getScoreStrategy(),
        gr.getScoreManner(),
        gr.getScorePunctuality(),
        gr.getScoreCommunity(),
        gr.getScorePoliteness(),
        gr.getScoreRematch(),
        gr.getComment(),
        gr.getUpdatedAt() != null
          ? gr.getUpdatedAt()
          : gr.getCreatedAt()
      );
    }).toList();

    return new PageImpl<>(content, pageable, rawPage.getTotalElements());
  }

  private static UserInfo getUserInfo(Object[] row) {
    UserEntity u = (UserEntity) row[2];
    UserTableTennisInfoEntity tti = (UserTableTennisInfoEntity) row[3];
    UserRankingEntity ranking = (UserRankingEntity) row[4];
    FriendEntity f = (FriendEntity) row[5];

    FriendStatus friendStatus = (f != null)
      ? f.getStatus()
      : FriendStatus.NOTHING;

    UserInfo userInfo = new UserInfo(
      u.getId(), u.getName(), u.getNickName(), u.getEmail(), u.getProfileImage(),
      tti.getRacketType(), tti.getUserLevel(),
      ranking.getRating(), ranking.getWinRate(), ranking.getTotalGames(), ranking.getWinCount(), ranking.getDefeatCount(), ranking.getLastGameAt(),
      friendStatus
    );
    return userInfo;
  }

  @Transactional
  public void updateGameReview(String gameReviewId, UpdateGameReviewRequestDTO updateGameReviewRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    GameReviewEntity gameReviewEntity = gameReviewRepository.findById(gameReviewId)
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_REVIEW_NOT_FOUND));

    if (!gameReviewEntity.getReviewerId().equals(userId)) {
      throw new CustomException(ErrorCode.GAME_REVIEW_EDIT_FORBIDDEN);
    }

    if (updateGameReviewRequestDTO.getScoreServe() != null) gameReviewEntity.setScoreServe(updateGameReviewRequestDTO.getScoreServe());
    if (updateGameReviewRequestDTO.getScoreReceive() != null) gameReviewEntity.setScoreReceive(updateGameReviewRequestDTO.getScoreReceive());
    if (updateGameReviewRequestDTO.getScoreRally() != null) gameReviewEntity.setScoreRally(updateGameReviewRequestDTO.getScoreRally());
    if (updateGameReviewRequestDTO.getScoreStrokes() != null) gameReviewEntity.setScoreStrokes(updateGameReviewRequestDTO.getScoreStrokes());
    if (updateGameReviewRequestDTO.getScoreStrategy() != null) gameReviewEntity.setScoreStrategy(updateGameReviewRequestDTO.getScoreStrategy());
    if (updateGameReviewRequestDTO.getScoreManner() != null) gameReviewEntity.setScoreManner(updateGameReviewRequestDTO.getScoreManner());
    if (updateGameReviewRequestDTO.getScorePunctuality() != null) gameReviewEntity.setScorePunctuality(updateGameReviewRequestDTO.getScorePunctuality());
    if (updateGameReviewRequestDTO.getScoreCommunity() != null) gameReviewEntity.setScoreCommunity(updateGameReviewRequestDTO.getScoreCommunity());
    if (updateGameReviewRequestDTO.getScorePoliteness() != null) gameReviewEntity.setScorePoliteness(updateGameReviewRequestDTO.getScorePoliteness());
    if (updateGameReviewRequestDTO.getScoreRematch() != null) gameReviewEntity.setScoreRematch(updateGameReviewRequestDTO.getScoreRematch());
    if (updateGameReviewRequestDTO.getComment() != null) gameReviewEntity.setComment(updateGameReviewRequestDTO.getComment());

    gameReviewRepository.save(gameReviewEntity);
  }

  @Transactional
  public void deleteReview(String gameReviewId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    GameReviewEntity gameReviewEntity = gameReviewRepository.findById(gameReviewId)
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_REVIEW_NOT_FOUND));

    if (!gameReviewEntity.getReviewerId().equals(userId)) {
      throw new CustomException(ErrorCode.GAME_REVIEW_DELETE_FORBIDDEN);
    }

    gameReviewRepository.delete(gameReviewEntity);
  }
}

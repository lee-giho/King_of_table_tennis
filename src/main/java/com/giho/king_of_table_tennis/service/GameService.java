package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.*;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.GameApplicationRepository;
import com.giho.king_of_table_tennis.repository.GameInfoRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameInfoRepository gameInfoRepository;

  private final GameStateRepository gameStateRepository;

  private final GameApplicationRepository gameApplicationRepository;

  private final UserRepository userRepository;

  @Transactional
  public BooleanResponseDTO createGame(CreateGameRequestDTO createGameRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    String gameInfoId = UUID.randomUUID().toString();

    GameInfoEntity gameInfoEntity = new GameInfoEntity();

    gameInfoEntity.setId(gameInfoId);
    gameInfoEntity.setGameSet(createGameRequestDTO.getGameSet());
    gameInfoEntity.setGameScore(createGameRequestDTO.getGameScore());
    gameInfoEntity.setPlace(createGameRequestDTO.getPlace());
    gameInfoEntity.setAcceptanceType(createGameRequestDTO.getAcceptanceType());
    gameInfoEntity.setGameDate(createGameRequestDTO.getGameDate());

    GameStateEntity gameStateEntity = new GameStateEntity();

    gameStateEntity.setGameInfoId(gameInfoId);
    gameStateEntity.setDefenderId(userId);
    gameStateEntity.setState(GameState.RECRUITING);

    gameInfoRepository.save(gameInfoEntity);

    gameStateRepository.save(gameStateEntity);

    return new BooleanResponseDTO(true);
  }

  public SelectedGameDateResponseDTO getSelectedGameDate(String tableTennisCourtId) {

    List<GameInfoEntity> upComingGames = gameInfoRepository.findAllByPlaceAndGameDateAfter(tableTennisCourtId, LocalDateTime.now());

    List<LocalDateTime> selectedGameDateList = upComingGames.stream()
      .map(GameInfoEntity::getGameDate)
      .toList();

    return new SelectedGameDateResponseDTO(selectedGameDateList);
  }

  @Transactional
  public BooleanResponseDTO gameParticipation(GameParticipationRequestDTO gameParticipationRequestDTO) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    GameInfoEntity gameInfoEntity = gameInfoRepository.findById(gameParticipationRequestDTO.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_INFO_NOT_FOUND));

    GameStateEntity gameStateEntity = gameStateRepository.findWithLockByGameInfoId(gameParticipationRequestDTO.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));

    if (gameInfoEntity.getAcceptanceType() == AcceptanceType.FCFS) { // 선착순
      if (gameStateEntity.getState() == GameState.RECRUITING) {
        gameStateEntity.setChallengerId(userId);
        gameStateEntity.setState(GameState.WAITING);

        gameStateRepository.save(gameStateEntity);
      } else {
        throw new CustomException(ErrorCode.GAME_NOT_RECRUITING);
      }
    } else { // 선택
      if (gameStateEntity.getState() == GameState.RECRUITING) {
        GameApplicationEntity gameApplicationEntity = new GameApplicationEntity();

        gameApplicationEntity.setId(UUID.randomUUID().toString());
        gameApplicationEntity.setGameInfoId(gameParticipationRequestDTO.getGameInfoId());
        gameApplicationEntity.setApplicantId(userId);
        gameApplicationEntity.setApplicationAt(LocalDateTime.now());

        gameApplicationRepository.save(gameApplicationEntity);
      } else {
        throw new CustomException(ErrorCode.GAME_NOT_RECRUITING);
      }
    }

    return new BooleanResponseDTO(true);
  }

  public RecruitingGameListDTO getRecruitingGameList(String tableTennisCourtId) {

    List<RecruitingGameDTO> recruitingGames = gameInfoRepository.findRecruitingGamesByPlaceAndDateAfter(tableTennisCourtId, LocalDateTime.now());

    return new RecruitingGameListDTO(recruitingGames);
  }

  public GameDetailInfo getGameDetailInfo(String gameInfoId) {

    GameInfoEntity gameInfoEntity = gameInfoRepository.findById(gameInfoId)
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_INFO_NOT_FOUND));

    GameStateEntity gameStateEntity = gameStateRepository.findByGameInfoId(gameInfoId)
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));

    List<String> userIds = new ArrayList<>();
    userIds.add(gameStateEntity.getDefenderId());
    if (gameStateEntity.getChallengerId() != null) {
      userIds.add(gameStateEntity.getChallengerId());
    }

    List<UserInfo> users = userRepository.findUserInfoByIds(userIds);

    Map<String, UserInfo> userMap = users.stream()
      .collect(Collectors.toMap(UserInfo::getId, user -> user));

    UserInfo defender = userMap.get(gameStateEntity.getDefenderId());
    UserInfo challenger = gameStateEntity.getChallengerId() != null
      ? userMap.get(gameStateEntity.getChallengerId())
      : null;

    return new GameDetailInfo(defender, challenger, gameInfoEntity, gameStateEntity);
  }
}

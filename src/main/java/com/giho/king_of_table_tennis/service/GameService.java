package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.*;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

  private final GameInfoRepository gameInfoRepository;

  private final GameStateRepository gameStateRepository;

  private final GameApplicationRepository gameApplicationRepository;

  private final UserRepository userRepository;

  private final TableTennisCourtRepository tableTennisCourtRepository;

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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    List<RecruitingGameDTO> recruitingGames = gameInfoRepository.findRecruitingGamesByPlaceAndDateAfter(tableTennisCourtId, LocalDateTime.now());

    for (RecruitingGameDTO dto : recruitingGames) {
      if (dto.getCreatorId().equals(userId)) {
        dto.setMine(true);
      }
    }

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

  public Page<GameDetailInfoByPage> getGameDetailInfoByPage(Pageable pageable, String place) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = authentication.getName();

    LocalDateTime now = LocalDateTime.now();

    Page<GameInfoEntity> gameInfoPage = gameInfoRepository
      .findByPlaceAndGameDateAfterOrderByGameDateAsc(place, now, pageable);

    if (gameInfoPage.isEmpty()) {
      return Page.empty(pageable);
    }

    List<GameInfoEntity> gameInfoList = gameInfoPage.getContent();
    List<String> gameInfoIdList = gameInfoList.stream().map(GameInfoEntity::getId).toList();

    List<GameStateEntity> gameStateList = gameStateRepository.findAllByGameInfoIdIn(gameInfoIdList);
    Map<String, GameStateEntity> gameStateByGameId = gameStateList.stream()
      .collect(Collectors.toMap(GameStateEntity::getGameInfoId, s -> s));

    Set<String> userIds = new HashSet<>();
    for (GameStateEntity s : gameStateList) {
      userIds.add(s.getDefenderId());
      if (s.getChallengerId() != null) {
        userIds.add(s.getChallengerId());
      }
    }
    List<UserInfo> userInfoList = userRepository.findUserInfoByIds(new ArrayList<>(userIds));
    Map<String, UserInfo> userById = userInfoList.stream()
      .collect(Collectors.toMap(UserInfo::getId, u -> u));

    List<String> placeIds = gameInfoList.stream()
      .map(GameInfoEntity::getPlace)
      .distinct()
      .toList();

    List<TableTennisCourtEntity> tableTennisCourtList = tableTennisCourtRepository.findAllById(placeIds);
    Map<String, String> placeNameById = tableTennisCourtList.stream()
      .collect(Collectors.toMap(TableTennisCourtEntity::getId, TableTennisCourtEntity::getName));

    List<GameDetailInfoByPage> gameDetailInfoList = new ArrayList<>();
    for (GameInfoEntity gameInfo : gameInfoList) {

      String placeName = placeNameById.getOrDefault(gameInfo.getPlace(), gameInfo.getPlace());
      GameInfoEntity cloneGameInfo = cloneWithPlaceName(gameInfo, placeName);

      GameStateEntity gameState = gameStateByGameId.get(gameInfo.getId());
      if (gameState == null) {
        continue;
      }

      UserInfo defender = userById.get(gameState.getDefenderId());
      UserInfo challenger = (gameState.getChallengerId() == null)
        ? null
        : userById.get(gameState.getChallengerId());

      boolean isMine = defender.getId().equals(userId);

      gameDetailInfoList.add(new GameDetailInfoByPage(defender, challenger, cloneGameInfo, gameState, isMine));
    }

    return new PageImpl<>(gameDetailInfoList, pageable, gameInfoPage.getTotalElements());
  }

  public Page<GameDetailInfoByPage> getGameDetailInfoByUser(String userId, String type, Pageable pageable) {

    List<GameState> gameStates = new ArrayList<>();
    if (type.equals("before")) {
      gameStates = List.of(GameState.RECRUITING, GameState.WAITING);
    } else {
      gameStates = List.of(GameState.END);
    }

    System.out.println("type" + type);
    System.out.println("GameState" + gameStates);

    Page<Object[]> rawPage = gameInfoRepository.findByUserIdAndGameState(userId, gameStates, pageable);
    if (rawPage.isEmpty()) {
      return Page.empty(pageable);
    }

    List<GameDetailInfoByPage> gameDetailInfoByPages = rawPage.getContent().stream()
      .map(row -> {
        GameInfoEntity g = (GameInfoEntity) row[0];
        GameStateEntity s = (GameStateEntity) row[1];
        UserEntity d = (UserEntity) row[2];
        UserEntity c = (UserEntity) row[3];
        UserTableTennisInfoEntity dTti = (UserTableTennisInfoEntity) row[4];
        UserTableTennisInfoEntity cTti = (UserTableTennisInfoEntity) row[5];

        UserInfo defenderInfo = toUserInfo(d, dTti);
        UserInfo challengerInfo = (c != null) ? toUserInfo(c, cTti) : null;

        boolean isMine = d.getId().equals(userId);

        return new GameDetailInfoByPage(
          defenderInfo,
          challengerInfo,
          g,
          s,
          isMine
        );
      }).toList();

    return new PageImpl<>(gameDetailInfoByPages, pageable, rawPage.getTotalElements());
  }

  private GameInfoEntity cloneWithPlaceName(GameInfoEntity src, String placeName) {
    GameInfoEntity g = new GameInfoEntity();
    g.setId(src.getId());
    g.setGameSet(src.getGameSet());
    g.setGameScore(src.getGameScore());
    g.setAcceptanceType(src.getAcceptanceType());
    g.setGameDate(src.getGameDate());
    g.setPlace(placeName);
    return g;
  }

  private UserInfo toUserInfo(UserEntity u, UserTableTennisInfoEntity tti) {
    String racketType = (tti != null) ? tti.getRacketType() : null;
    String userLevel = (tti != null) ? tti.getUserLevel() : null;

    int winCount = 0;
    int defeatCount = 0;

    return new UserInfo(
      u.getId(),
      u.getName(),
      u.getNickName(),
      u.getEmail(),
      u.getProfileImage(),
      racketType,
      userLevel,
      winCount,
      defeatCount
    );
  }
}

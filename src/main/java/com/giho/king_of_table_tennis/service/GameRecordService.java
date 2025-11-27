package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.*;
import com.giho.king_of_table_tennis.entity.TableTennisCourtEntity;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.GameRecordRepository;
import com.giho.king_of_table_tennis.repository.TableTennisCourtRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameRecordService {

  private final GameRecordRepository gameRecordRepository;

  private final TableTennisCourtRepository tableTennisCourtRepository;

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public PageResponse<GameRecordInfo> getUserGameRecords(String userId, int page ,int size) {

    Pageable pageable = PageRequest.of(page, size);

    Page<GameRecordRow> rows = gameRecordRepository.findGameRecordsByUser(userId, pageable);

    List<String> placeIds = rows.getContent().stream()
      .map(GameRecordRow::getPlace)
      .distinct()
      .toList();

    Map<String, String> placeNameById = tableTennisCourtRepository.findAllById(placeIds)
      .stream()
      .collect(Collectors.toMap(TableTennisCourtEntity::getId, TableTennisCourtEntity::getName));

    List<GameRecordInfo> content = rows.getContent().stream()
      .map(row -> {
        // 내 정보
        GameUserInfo myInfo = new GameUserInfo(
          row.getMyId(),
          row.getMyNickName(),
          row.getMyProfileImage(),
          row.getMyRacketType()
        );
        myInfo.setSetScore(row.getMySetScore() != null ? row.getMySetScore() : 0);

        // 상대 정보
        GameUserInfo opponentInfo = new GameUserInfo(
          row.getOpponentId(),
          row.getOpponentNickName(),
          row.getOpponentProfileImage(),
          row.getOpponentRacketType()
        );
        opponentInfo.setSetScore(row.getOpponentSetScore() != null ? row.getOpponentSetScore() : 0);

        String placeName = placeNameById.getOrDefault(row.getPlace(), row.getPlace());

        // 승패 여부 계산
        boolean isWin = myInfo.getSetScore() > opponentInfo.getSetScore();

        return new GameRecordInfo(
          myInfo,
          opponentInfo,
          row.getGameInfoId(),
          row.getGameDate(),
          placeName,
          isWin
        );
      }).toList();

    return new PageResponse<>(
      content,
      rows.getTotalPages(),
      rows.getTotalElements(),
      rows.getNumber(),
      rows.getSize()
    );
  }

  @Transactional(readOnly = true)
  public UserGameRecordsStatsResponse getUserGameRecordsStats(String userId) {
    UserEntity userEntity = userRepository.findById(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    // 전체 전적 조회
    GameStatsProjection total = gameRecordRepository.findTotalStats(userId)
      .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    GameStats totalStats = new GameStats(
      total.getTotalGames(),
      total.getWinCount(),
      total.getDefeatCount(),
      total.getWinRate()
    );

    // 최근 10경기 전적 조회
    GameStatsProjection recent = gameRecordRepository.findRecentStats(userId);

    GameStats recentStats = new GameStats(
      recent.getTotalGames(),
      recent.getWinCount(),
      recent.getDefeatCount(),
      recent.getWinRate()
    );

    return new UserGameRecordsStatsResponse(
      userEntity.getNickName(),
      userEntity.getProfileImage(),
      totalStats,
      recentStats
    );
  }
}

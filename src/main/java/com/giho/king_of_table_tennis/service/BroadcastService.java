package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.BooleanResponseDTO;
import com.giho.king_of_table_tennis.dto.BroadcastRoomInfo;
import com.giho.king_of_table_tennis.dto.BroadcastRoomRequest;
import com.giho.king_of_table_tennis.dto.GameUserInfo;
import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import com.giho.king_of_table_tennis.entity.GameState;
import com.giho.king_of_table_tennis.entity.GameStateEntity;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.repository.BroadcastRoomRepository;
import com.giho.king_of_table_tennis.repository.GameInfoRepository;
import com.giho.king_of_table_tennis.repository.GameStateRepository;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BroadcastService {

  private final GameInfoRepository gameInfoRepository;

  private final GameStateRepository gameStateRepository;

  private final UserRepository userRepository;

  private final BroadcastRoomRepository broadcastRoomRepository;

  public BroadcastRoomInfo saveBroadcastRoom(BroadcastRoomRequest createBroadcastRoomRequest) {

    GameInfoEntity gameInfoEntity = gameInfoRepository.findById(createBroadcastRoomRequest.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_INFO_NOT_FOUND));

    GameStateEntity gameStateEntity = gameStateRepository.findByGameInfoId(createBroadcastRoomRequest.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));

    List<String> userIds = Arrays.asList(gameStateEntity.getDefenderId(), gameStateEntity.getChallengerId());
    List<GameUserInfo> users = userRepository.findGameUserInfoByIds(userIds);

    GameUserInfo defender = users.stream()
      .filter(u -> u.getId().equals(gameStateEntity.getDefenderId()))
      .findFirst()
      .orElse(null);

    GameUserInfo challenger = users.stream()
      .filter(u -> u.getId().equals(gameStateEntity.getChallengerId()))
      .findFirst()
      .orElse(null);

    String broadcastRoomName = defender.getNickName() + " VS " + challenger.getNickName();

    BroadcastRoomInfo broadcastRoomInfo = new BroadcastRoomInfo(
      createBroadcastRoomRequest.getGameInfoId(),
      broadcastRoomName,
      defender,
      challenger
    );

    broadcastRoomRepository.saveRoom(broadcastRoomInfo);

    return broadcastRoomInfo;
  }

  public BroadcastRoomInfo findBroadcast(String gameInfoId) {
    BroadcastRoomInfo broadcastRoomInfo = broadcastRoomRepository.findRoom(gameInfoId)
      .orElseThrow(() -> new CustomException(ErrorCode.BROADCAST_ROOM_NOT_FOUND));
    return broadcastRoomInfo;
  }

  public BooleanResponseDTO deleteBroadcast(BroadcastRoomRequest broadcastRoomRequest) {
    broadcastRoomRepository.deleteRoom(broadcastRoomRequest.getGameInfoId());

    GameStateEntity gameStateEntity = gameStateRepository.findByGameInfoId(broadcastRoomRequest.getGameInfoId())
      .orElseThrow(() -> new CustomException(ErrorCode.GAME_STATE_NOT_FOUND));

    gameStateEntity.setState(GameState.END);

    gameStateRepository.save(gameStateEntity);

    return new BooleanResponseDTO(true);
  }
}

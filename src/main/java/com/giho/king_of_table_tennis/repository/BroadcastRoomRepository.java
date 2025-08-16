package com.giho.king_of_table_tennis.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giho.king_of_table_tennis.dto.BroadcastRoomInfo;
import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

@Repository
@RequiredArgsConstructor
public class BroadcastRoomRepository {

  private final String ROOM_KEY_PREFIX = "broadcast_room:";

  private final StringRedisTemplate stringRedisTemplate;
  private final ObjectMapper objectMapper;

  private String getKey(String gameInfoId) {
    return ROOM_KEY_PREFIX + gameInfoId;
  }

  private BroadcastRoomInfo readJson(String json) {
    try {
      return objectMapper.readValue(json, BroadcastRoomInfo.class);
    } catch (IOException e) {
      throw new CustomException(ErrorCode.JSON_PARSE_FAILED);
    }
  }

  private String writeJson(BroadcastRoomInfo broadcastRoomInfo) {
    try {
      return objectMapper.writeValueAsString(broadcastRoomInfo);
    } catch (JsonProcessingException e) {
      throw new CustomException(ErrorCode.JSON_SERIALIZE_FAILED);
    }
  }

  // 저장
  public void saveRoom(BroadcastRoomInfo broadcastRoomInfo) {
    String key = getKey(broadcastRoomInfo.getGameInfoId());
    String json = writeJson(broadcastRoomInfo);
    stringRedisTemplate.opsForValue().set(key, json);
  }

  // 조회
  public Optional<BroadcastRoomInfo> findRoom(String gameInfoId) {
    String json = stringRedisTemplate.opsForValue().get(getKey(gameInfoId));
    if (json.isEmpty()) return Optional.empty();
    return Optional.of(readJson(json));
  }

  // 존재 여부
  public boolean exists(String roomId) {
    Boolean exists = stringRedisTemplate.hasKey(getKey(roomId));
    return Boolean.TRUE.equals(exists);
  }

  // 삭제
  public void deleteRoom(String roomId) {
    stringRedisTemplate.delete(getKey(roomId));
  }

  // 부분 업데이트
  public void patchRoom(String roomId, Consumer<BroadcastRoomInfo> updater) {
    BroadcastRoomInfo current = findRoom(roomId)
      .orElseThrow(() -> new CustomException(ErrorCode.BROADCAST_ROOM_NOT_FOUND));
    updater.accept(current);
    saveRoom(current);
  }
}

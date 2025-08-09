package com.giho.king_of_table_tennis.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giho.king_of_table_tennis.dto.BroadcastRoomInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class BroadcastRoomRepository {

  private final String ROOM_KEY_PREFIX = "broadcast_room:";

  private final StringRedisTemplate stringRedisTemplate;
  private final ObjectMapper objectMapper;

  public void saveRoom(BroadcastRoomInfo broadcastRoomInfo) {
    String key = ROOM_KEY_PREFIX + broadcastRoomInfo.getRoomId();
    try {
      String json = objectMapper.writeValueAsString(broadcastRoomInfo);
      stringRedisTemplate.opsForValue().set(key, json);
    } catch (JsonProcessingException e) {
      throw new IllegalStateException("BroadcastRoomInfo를 json으로 변환하는 중 오류가 발생했습니다.");
    }
  }
}

package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.UserBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBlockRepository extends JpaRepository<UserBlockEntity, String> {
  boolean existsByBlockerIdAndBlockedId(String blockerId, String blockedId);

  Optional<UserBlockEntity> findByBlockerIdAndBlockedId(String blockerId, String blockedId);
}

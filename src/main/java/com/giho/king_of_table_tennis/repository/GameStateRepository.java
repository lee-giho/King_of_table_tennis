package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameStateEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;
import java.util.Optional;

public interface GameStateRepository extends JpaRepository<GameStateEntity, String> {

  Optional<GameStateEntity> findByGameInfoId(String gameInfoId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Optional<GameStateEntity> findWithLockByGameInfoId(String gameInfoId);

  List<GameStateEntity> findAllByGameInfoIdIn(List<String> gameInfoIds);

  void deleteByGameInfoId(String gameInfoId);
}

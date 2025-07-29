package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface GameInfoRepository extends JpaRepository<GameInfoEntity, String> {

  List<GameInfoEntity> findAllByPlaceAndGameDateAfter(String place, LocalDateTime now);
}

package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameInfoRepository extends JpaRepository<GameInfoEntity, String> {
}

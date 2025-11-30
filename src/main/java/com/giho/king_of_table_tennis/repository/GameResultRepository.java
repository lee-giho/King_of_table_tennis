package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameResultRepository extends JpaRepository<GameResultEntity, String> {
}

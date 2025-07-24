package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStateRepository extends JpaRepository<GameStateEntity, String> {
}

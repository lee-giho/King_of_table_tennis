package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameReviewRepository extends JpaRepository<GameReviewEntity, String> {

  boolean existsByGameInfoIdAndReviewerIdAndRevieweeId(String gameInfoId, String reviewerId, String revieweeId);
}

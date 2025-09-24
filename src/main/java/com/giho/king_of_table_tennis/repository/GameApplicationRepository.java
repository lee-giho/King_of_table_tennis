package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameApplicationRepository extends JpaRepository<GameApplicationEntity, String> {
  Optional<GameApplicationEntity> findByGameInfoIdAndApplicantId(String gameInfoId, String applicantId);
}

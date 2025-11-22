package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.UserRankingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRankingRepository extends JpaRepository<UserRankingEntity, String> {
}

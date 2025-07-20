package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.UserTableTennisInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTableTennisInfoRepository extends JpaRepository<UserTableTennisInfoEntity, String> {

  boolean existsByUserId(String userId);
}

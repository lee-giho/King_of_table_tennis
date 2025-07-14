package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsById(String id);

  boolean existsByNickName(String nickName);

  Optional<UserEntity> findByNameAndEmail(String name, String email);
}

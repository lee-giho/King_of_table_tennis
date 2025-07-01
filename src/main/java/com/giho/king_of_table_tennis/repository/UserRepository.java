package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {


}

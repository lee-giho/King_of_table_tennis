package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.FriendEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<FriendEntity, String> {
}

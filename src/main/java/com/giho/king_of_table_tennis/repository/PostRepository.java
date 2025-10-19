package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<PostEntity, String> {
}

package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {
}

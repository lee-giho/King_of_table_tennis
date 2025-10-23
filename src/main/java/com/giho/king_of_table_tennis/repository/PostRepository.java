package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.PostDTO;
import com.giho.king_of_table_tennis.entity.PostCategory;
import com.giho.king_of_table_tennis.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.PostDTO(
      p.id,
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      p.title, p.category, p.content,
      COALESCE(p.updatedAt, p.createdAt),
      CASE WHEN p.updatedAt IS NOT NULL THEN true ELSE false END
    )
    FROM PostEntity p
    JOIN UserEntity u ON u.id = p.writerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    WHERE p.writerId = :writerId
    ORDER BY p.createdAt DESC
  """)
  Page<PostDTO> findAllByWriterId(@Param("writerId") String writerId, Pageable pageable);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.PostDTO(
      p.id,
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount,
      p.title, p.category, p.content,
      COALESCE(p.updatedAt, p.createdAt),
      CASE WHEN p.updatedAt IS NOT NULL THEN true ELSE false END
    )
    FROM PostEntity p
    JOIN UserEntity u ON u.id = p.writerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    WHERE p.category IN :categories
  """)
  Page<PostDTO> findAllPostDTOByCategoryIn(@Param("categories") List<PostCategory> categories, Pageable pageable);
}

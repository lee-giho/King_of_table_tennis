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
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status,
      p.title, p.category, p.content, p.createdAt, p.updatedAt,
      CASE WHEN p.writerId = :currentUserId THEN true ELSE false END
    )
    FROM PostEntity p
      JOIN UserEntity u ON u.id = p.writerId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
      LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE p.writerId = :writerId
  """)
  Page<PostDTO> findAllByWriterId(
    @Param("writerId") String writerId,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.PostDTO(
      p.id,
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status,
      p.title, p.category, p.content, p.createdAt, p.updatedAt,
      CASE WHEN p.writerId = :currentUserId THEN true ELSE false END
    )
    FROM PostEntity p
      JOIN UserEntity u ON u.id = p.writerId
      LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
      LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
      LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE p.category IN :categories
      AND (
        :keyword IS NULL
        OR :keyword = ''
        OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
  """)
  Page<PostDTO> findAllPostDTOByCategoryInAndKeyword(
    @Param("categories") List<PostCategory> categories,
    @Param("currentUserId") String currentUserId,
    @Param("keyword") String keyword,
    Pageable pageable
  );
}

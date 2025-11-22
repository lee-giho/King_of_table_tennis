package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.CommentDTO;
import com.giho.king_of_table_tennis.entity.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, String> {

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.CommentDTO(
      c.id, c.postId,
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status,
      c.content, c.createdAt, c.updatedAt,
      CASE WHEN c.writerId = :currentUserId THEN true ELSE false END
    )
    FROM CommentEntity c
    JOIN UserEntity u ON u.id = c.writerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE c.postId = :postId
  """)
  Page<CommentDTO> findAllCommentDTOByPostId(
    @Param("postId") String postId,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.CommentDTO(
      c.id, c.postId,
      u.id, u.name, u.nickName, u.email, u.profileImage,
      tti.racketType, tti.userLevel,
      ranking.rating, ranking.winRate, ranking.totalGames, ranking.winCount, ranking.defeatCount, ranking.lastGameAt,
      f.status,
      c.content, c.createdAt, c.updatedAt,
      CASE WHEN c.writerId = :currentUserId THEN true ELSE false END
    )
    FROM CommentEntity c
    JOIN UserEntity u ON u.id = c.writerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN UserRankingEntity ranking ON ranking.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE c.writerId = :currentUserId
  """)
  Page<CommentDTO> findAllCommentDTOByUserId(
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );
}

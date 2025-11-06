package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.GameReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameReviewRepository extends JpaRepository<GameReviewEntity, String> {

  boolean existsByGameInfoIdAndReviewerIdAndRevieweeId(String gameInfoId, String reviewerId, String revieweeId);

  // 내가 작성한 리뷰 가져오기
  @Query("""
    SELECT gr, g, u, tti, f
    FROM GameReviewEntity gr
    JOIN GameInfoEntity g ON g.id = gr.gameInfoId
    JOIN UserEntity u ON u.id = gr.revieweeId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE gr.reviewerId = :reviewerId
    ORDER BY gr.createdAt DESC
  """)
  Page<Object[]> findAllWrittenReviewDetails(
    @Param("reviewerId") String reviewerId,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );

  // 내가 받은 리뷰 가져오기
  @Query("""
    SELECT gr, g, u, tti, f
    FROM GameReviewEntity gr
    JOIN GameInfoEntity g ON g.id = gr.gameInfoId
    JOIN UserEntity u ON u.id = gr.reviewerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    LEFT JOIN FriendEntity f ON f.userId = :currentUserId AND f.friendId = u.id
    WHERE gr.revieweeId = :revieweeId
    ORDER BY gr.createdAt DESC
  """)
  Page<Object[]> findAllReceivedReviewDetails(
    @Param("revieweeId") String revieweeId,
    @Param("currentUserId") String currentUserId,
    Pageable pageable
  );
}

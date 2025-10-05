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
    SELECT gr, u, tti, g
    FROM GameReviewEntity gr
    JOIN UserEntity u ON u.id = gr.revieweeId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    JOIN GameInfoEntity g ON g.id = gr.gameInfoId
    WHERE gr.reviewerId = :reviewerId
    ORDER BY gr.createdAt DESC
  """)
  Page<Object[]> findAllWrittenReviewDetails(@Param("reviewerId") String reviewerId, Pageable pageable);

  // 내가 받은 리뷰 가져오기
  @Query("""
    SELECT gr, u, tti, g
    FROM GameReviewEntity gr
    JOIN UserEntity u ON u.id = gr.reviewerId
    LEFT JOIN UserTableTennisInfoEntity tti ON tti.userId = u.id
    JOIN GameInfoEntity g ON g.id = gr.gameInfoId
    WHERE gr.revieweeId = :revieweeId
    ORDER BY gr.createdAt DESC
  """)
  Page<Object[]> findAllReceivedReviewDetails(@Param("revieweeId") String revieweeId, Pageable pageable);
}

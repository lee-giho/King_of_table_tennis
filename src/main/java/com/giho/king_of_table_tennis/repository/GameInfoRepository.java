package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.GameDetailInfo;
import com.giho.king_of_table_tennis.dto.RecruitingGameDTO;
import com.giho.king_of_table_tennis.entity.GameInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface GameInfoRepository extends JpaRepository<GameInfoEntity, String> {

  List<GameInfoEntity> findAllByPlaceAndGameDateAfter(String place, LocalDateTime now);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.RecruitingGameDTO(g, s.defenderId, s.state)
    FROM GameInfoEntity g
    JOIN GameStateEntity s ON g.id = s.gameInfoId
    WHERE g.place = :place
    AND g.gameDate > :now
  """)
  List<RecruitingGameDTO> findRecruitingGamesByPlaceAndDateAfter(@Param("place") String place, @Param("now") LocalDateTime now);

  Page<GameInfoEntity> findByPlaceAndGameDateAfterOrderByGameDateAsc(String place, LocalDateTime now, Pageable pageable);
}

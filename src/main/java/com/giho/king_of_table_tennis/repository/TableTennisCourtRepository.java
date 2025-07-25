package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.TableTennisCourtEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TableTennisCourtRepository extends MongoRepository<TableTennisCourtEntity, String> {
  Optional<TableTennisCourtEntity> findByName(String name);

  List<TableTennisCourtEntity> findAllByNameContainingIgnoreCase(String name);
}

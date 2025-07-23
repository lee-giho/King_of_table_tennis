package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.entity.TableTennisCourt;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TableTennisCourtRepository extends MongoRepository<TableTennisCourt, String> {
  Optional<TableTennisCourt> findByName(String name);
}

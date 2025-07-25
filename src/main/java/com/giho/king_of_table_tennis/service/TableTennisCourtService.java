package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.TableTennisCourtResponseDTO;
import com.giho.king_of_table_tennis.entity.TableTennisCourtEntity;
import com.giho.king_of_table_tennis.repository.TableTennisCourtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class TableTennisCourtService {

  private final TableTennisCourtRepository tableTennisCourtRepository;

  public TableTennisCourtResponseDTO searchTableTennisCourtByName(String name) {
    List<TableTennisCourtEntity> tableTennisCourts = tableTennisCourtRepository.findAllByNameContainingIgnoreCase(name);

    return new TableTennisCourtResponseDTO(tableTennisCourts);
  }
}

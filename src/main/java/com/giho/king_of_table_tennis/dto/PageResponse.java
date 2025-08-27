package com.giho.king_of_table_tennis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PageResponse<T> {
  private List<T> content;
  private int totalPages;
  private long totalElements;
  private int pageNumber;
  private int pageSize;
}

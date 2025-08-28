package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "페이징을 통해 반환할 때 감싸서 응답하는 DTO")
public class PageResponse<T> {
  private List<T> content;
  private int totalPages;
  private long totalElements;
  private int pageNumber;
  private int pageSize;
}

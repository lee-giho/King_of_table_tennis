package com.giho.king_of_table_tennis.controller;

import com.giho.king_of_table_tennis.dto.TableTennisCourtResponseDTO;
import com.giho.king_of_table_tennis.service.TableTennisCourtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "TableTennisCourt-Controller", description = "탁구장 관련 API")
@RequestMapping("/api/ttc")
public class TableTennisCourtController {

  private final TableTennisCourtService tableTennisCourtService;

  @Operation(summary = "탁구장 검색", description = "name으로 탁구장들을 검색하는 API", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
    responseCode = "200",
    description = "탁구장 리스트 반환",
    content = @Content(
      mediaType = "application/json",
      schema = @Schema(implementation = TableTennisCourtResponseDTO.class)
    )
  )
  @GetMapping("/{name}")
  public ResponseEntity<TableTennisCourtResponseDTO> searchTableTennisCourtByName(@PathVariable String name) {
    TableTennisCourtResponseDTO tableTenniscourtResponseDTO = tableTennisCourtService.searchTableTennisCourtByName(name);
    return ResponseEntity.ok(tableTenniscourtResponseDTO);
  }
}

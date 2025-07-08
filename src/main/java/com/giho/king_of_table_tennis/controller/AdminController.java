package com.giho.king_of_table_tennis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@Tag(name = "Admin-Controller", description = "Admin API 엔드포인트")
public class AdminController {

  @GetMapping("/admin")
  @Operation(summary = "Admin 접근 테스트", description = "ROLE_ADMIN을 가진 어드민 계정만 접근할 수 있습니다.", security = @SecurityRequirement(name = "JWT"))
  @ApiResponse(
      responseCode = "200",
      description = "접근 성공",
      content = @Content(
        mediaType = "text/plain",
        schema = @Schema(type = "string", example = "admin controller")
    )
  )
  public String adminP() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String id = authentication.getName();
    System.out.println("id: " + id);
    return "admin controller";
  }
}

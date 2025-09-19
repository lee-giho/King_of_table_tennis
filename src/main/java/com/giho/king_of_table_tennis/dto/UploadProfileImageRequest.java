package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "프로필 사진 변경 요청을 위한 DTO")
public class UploadProfileImageRequest {
  private MultipartFile profileImage;
}

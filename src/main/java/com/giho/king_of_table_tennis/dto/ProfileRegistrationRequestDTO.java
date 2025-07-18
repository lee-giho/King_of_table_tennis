package com.giho.king_of_table_tennis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "profileImage와 nickName 등록 요청 DTO")
public class ProfileRegistrationRequestDTO {
  private MultipartFile profileImage;
  private String nickName;
}

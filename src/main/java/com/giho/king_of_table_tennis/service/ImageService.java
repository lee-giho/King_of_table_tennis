package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

  @Value("${PROFILE_IMAGE_PATH}")
  private String profileImageRelativePath;

  public String saveImage(MultipartFile image) {
    String imageFileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
    String fullPath = profileImageRelativePath + imageFileName;

    try {
      File dest = new File(fullPath);
      File parentDir = dest.getParentFile();
      if (!parentDir.exists()) {
        boolean isCreated = parentDir.mkdirs();
        if (!isCreated) {
          throw new CustomException(ErrorCode.DIR_CREATE_ERROR);
        }
      }

      image.transferTo(dest);

    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_STORAGE_ERROR);
    }

    return imageFileName;
  }
}

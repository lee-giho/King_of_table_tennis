package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.exception.CustomException;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

  private final S3Client s3Client;

  @Value("${spring.cloud.aws.s3.bucket}")
  private String bucketName;

  // S3에 이미지 저장 후 object key 반환
  public String saveImage(MultipartFile image, String userId) {
    if (image == null || image.isEmpty()) {
      throw new CustomException(ErrorCode.FILE_STORAGE_ERROR);
    }

    String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
    String key = "profile/" + userId + "/" + fileName;

    try {
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .contentType(image.getContentType())
        .build();

      s3Client.putObject(
        putObjectRequest,
        RequestBody.fromInputStream(image.getInputStream(), image.getSize())
      );

      return key;

    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_STORAGE_ERROR);
    }
  }

  // S3 프로필 이미지 삭제
  public void deleteProfileImage(String key) {
    if (key == null || key.isBlank() || "default".equals(key)) {
      return;
    }

    try {
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
        .bucket(bucketName)
        .key(key)
        .build();

      s3Client.deleteObject(deleteObjectRequest);
    } catch (Exception e) {
      throw new CustomException(ErrorCode.FILE_DELETE_ERROR);
    }
  }
}

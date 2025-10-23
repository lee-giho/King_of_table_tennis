package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.PostCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "게시글 정보를 반환하는 DTO")
public class PostDTO {
  private String id;
  private UserInfo writer;

  private String title;
  private PostCategory category;

  private String content;

  private LocalDateTime writeAt;

  private boolean isUpdated;

  public PostDTO(
    String id,
    String writerId,
    String writerName,
    String writerNickName,
    String writerEmail,
    String writerProfileImage,
    String racketType,
    String userLevel,
    Integer winCount,
    Integer defeatCount,
    String title,
    PostCategory category,
    String content,
    LocalDateTime writeAt,
    boolean isUpdated
  ) {
    this.id = id;
    this.writer = new UserInfo(
      writerId, writerName, writerNickName, writerEmail, writerProfileImage,
      racketType, userLevel,
      winCount != null ? winCount : 0,
      defeatCount != null ? defeatCount : 0
    );
    this.title = title;
    this.category = category;
    this.content = content;
    this.writeAt = writeAt;
    this.isUpdated = isUpdated;
  }
}

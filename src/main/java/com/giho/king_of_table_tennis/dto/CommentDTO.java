package com.giho.king_of_table_tennis.dto;

import com.giho.king_of_table_tennis.entity.FriendStatus;
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
@Schema(description = "댓글 정보를 반환하는 DTO")
public class CommentDTO {
  private String id;
  private String postId;

  private UserInfo writer;

  private String content;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  private boolean isMine;

  public CommentDTO(
    String id,
    String postId,
    String writerId,
    String writerName,
    String writerNickName,
    String writerEmail,
    String writerProfileImage,
    String racketType,
    String userLevel,
    Integer winCount,
    Integer defeatCount,
    FriendStatus friendStatus,
    String content,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    boolean isMine
  ) {
    this.id = id;
    this.postId = postId;
    this.writer = new UserInfo(
      writerId, writerName, writerNickName, writerEmail, writerProfileImage,
      racketType, userLevel,
      winCount != null ? winCount : 0,
      defeatCount != null ? defeatCount : 0,
      friendStatus
    );
    this.content = content;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.isMine = isMine;
  }
}

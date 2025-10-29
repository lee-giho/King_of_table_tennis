package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "comment")
public class CommentEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "post_id", nullable = false)
  private String postId;

  @Column(name = "writer_id", nullable = false)
  private String writerId;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", updatable = false, insertable = false)
  private LocalDateTime updatedAt;
}

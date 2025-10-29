package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "post")
public class PostEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "writer_id", nullable = false)
  private String writerId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "category", nullable = false)
  @Enumerated(EnumType.STRING)
  private PostCategory category;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_at", updatable = false, insertable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", updatable = false, insertable = false)
  private LocalDateTime updatedAt;
}

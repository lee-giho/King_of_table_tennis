package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_table_tennis_info")
public class UserTableTennisInfoEntity {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(name = "racket_type", nullable = false)
  private String racketType;

  @Column(name = "user_level", nullable = false)
  private String userLevel;
}

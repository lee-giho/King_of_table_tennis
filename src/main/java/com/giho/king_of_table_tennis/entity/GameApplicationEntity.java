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
@Table(name = "game_application")
public class GameApplicationEntity {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "game_info_id", nullable = false)
  private String gameInfoId;

  @Column(name = "applicant_id", nullable = false)
  private String applicantId;

  @Column(name = "application_at", nullable = false)
  private LocalDateTime applicationAt;
}

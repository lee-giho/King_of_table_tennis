package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserEntity {

  @Id
  private String id;

  private String password;

  private String name;

  private String nickName;

  private String email;

  private String profileImage;

  private String role;
}

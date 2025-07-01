package com.giho.king_of_table_tennis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserEntity {

  @Id
  @Column(name = "id", nullable = false, unique = true)
  private String id;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "nick_name", nullable = false)
  private String nickName;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "profile_image", nullable = false)
  private String profileImage;

  @Column(name = "role", nullable = false)
  private String role;
}

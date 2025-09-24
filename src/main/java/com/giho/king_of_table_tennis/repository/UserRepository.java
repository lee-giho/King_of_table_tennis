package com.giho.king_of_table_tennis.repository;

import com.giho.king_of_table_tennis.dto.GameUserInfo;
import com.giho.king_of_table_tennis.dto.MySimpleInfoResponse;
import com.giho.king_of_table_tennis.dto.UserInfo;
import com.giho.king_of_table_tennis.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  boolean existsById(String id);

  boolean existsByNickName(String nickName);

  Optional<UserEntity> findByNameAndEmail(String name, String email);

  Optional<UserEntity> findByIdAndNameAndEmail(String id, String name, String email);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(u.id, u.name, u.nickName, u.email, u.profileImage, tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount)
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id IN :userIds
  """)
  List<UserInfo> findUserInfoByIds(@Param("userIds") List<String> userIds);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.UserInfo(u.id, u.name, u.nickName, u.email, u.profileImage, tti.racketType, tti.userLevel, tti.winCount, tti.defeatCount)
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id = :id
  """)
  UserInfo findUserInfoById(String id);

  List<UserEntity> findByIdIn(List<String> userIds);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.GameUserInfo(u.id, u.nickName, u.profileImage, tti.racketType)
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id IN :userIds
  """)
  List<GameUserInfo> findGameUserInfoByIds(@Param("userIds") List<String> userIds);

  @Query("""
    SELECT new com.giho.king_of_table_tennis.dto.MySimpleInfoResponse(u.nickName, u.profileImage, tti.racketType, tti.winCount, tti.defeatCount)
    FROM UserEntity u JOIN UserTableTennisInfoEntity tti ON u.id = tti.userId
    WHERE u.id = :id
  """)
  MySimpleInfoResponse findMySimpleInfoById(String id);
}

package com.giho.king_of_table_tennis;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KingOfTableTennisApplication {

  public static void main(String[] args) {

    // .env 파일 로드
    Dotenv dotenv = Dotenv.configure().load();

    System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
    System.setProperty("MONGODB_URL", dotenv.get("MONGODB_URL"));
    System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
    System.setProperty("ACCESS_TOKEN_EXP", dotenv.get("ACCESS_TOKEN_EXP"));
    System.setProperty("REFRESH_TOKEN_EXP", dotenv.get("REFRESH_TOKEN_EXP"));
    System.setProperty("GOOGLE_MAIL_PASSWORD", dotenv.get("GOOGLE_MAIL_PASSWORD"));
    System.setProperty("PROFILE_IMAGE_PATH", dotenv.get("PROFILE_IMAGE_PATH"));

    SpringApplication.run(KingOfTableTennisApplication.class, args);
  }

}

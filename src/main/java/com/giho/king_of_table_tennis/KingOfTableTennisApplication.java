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

    SpringApplication.run(KingOfTableTennisApplication.class, args);
  }

}

package com.giho.king_of_table_tennis;

import com.giho.king_of_table_tennis.util.EnvLoader;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KingOfTableTennisApplication {

  public static void main(String[] args) {

    EnvLoader.loadEnv();

    SpringApplication.run(KingOfTableTennisApplication.class, args);
  }

}

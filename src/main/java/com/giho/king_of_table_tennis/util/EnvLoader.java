package com.giho.king_of_table_tennis.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvLoader {

  private static boolean isLoaded = false;

  public static void loadEnv() {
    if (isLoaded) return;

    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    System.setProperty("MYSQL_PASSWORD", dotenv.get("MYSQL_PASSWORD"));
    System.setProperty("MONGODB_URL", dotenv.get("MONGODB_URL"));
    System.setProperty("JWT_SECRET_KEY", dotenv.get("JWT_SECRET_KEY"));
    System.setProperty("ACCESS_TOKEN_EXP", dotenv.get("ACCESS_TOKEN_EXP"));
    System.setProperty("REFRESH_TOKEN_EXP", dotenv.get("REFRESH_TOKEN_EXP"));
    System.setProperty("GOOGLE_MAIL_PASSWORD", dotenv.get("GOOGLE_MAIL_PASSWORD"));
    System.setProperty("PROFILE_IMAGE_PATH", dotenv.get("PROFILE_IMAGE_PATH"));

    isLoaded = true;
  }
}

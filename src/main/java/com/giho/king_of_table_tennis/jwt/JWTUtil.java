package com.giho.king_of_table_tennis.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

  private SecretKey secretKey;

  public JWTUtil(@Value("${JWT_SECRET_KEY}") String secret) {
    this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  public String getCategory(String token) {
    return Jwts.parser()
      .verifyWith(secretKey).build()
      .parseSignedClaims(token).getPayload()
      .get("category", String.class);
  }

  public String getUserId(String token) {
    return Jwts.parser()
      .verifyWith(secretKey).build()
      .parseSignedClaims(token).getPayload()
      .get("id", String.class);
  }

  public String getRole(String token) {
    return Jwts.parser()
      .verifyWith(secretKey).build()
      .parseSignedClaims(token).getPayload()
      .get("role", String.class);
  }

  public boolean isExpired(String token) {
    try {
      Date exp = Jwts.parser()
        .verifyWith(secretKey).build()
        .parseSignedClaims(token)
        .getPayload()
        .getExpiration();

      return exp.before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public String createJwt(String category, String id, String role, Long expiredMs) {

    Date now = new Date();

    return Jwts.builder()
      .claim("category", category)
      .claim("id", id)
      .claim("role", role)
      .issuedAt(now)
      .expiration(new Date(now.getTime() + expiredMs))
      .signWith(secretKey)
      .compact();
  }

  public String getTokenWithoutBearer(String token) {
    return token.substring(7);
  }
}

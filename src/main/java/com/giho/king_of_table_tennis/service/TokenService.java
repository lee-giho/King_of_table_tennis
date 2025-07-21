package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.RefreshAccessTokenResponseDTO;
import com.giho.king_of_table_tennis.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

  private final JWTUtil jwtUtil;

  @Value("${ACCESS_TOKEN_EXP}")
  private long accessTokenExp;

  public RefreshAccessTokenResponseDTO refreshAccessTokenByRefreshToken(String token) {
    String refreshToken = jwtUtil.getTokenWithoutBearer(token);

    String userId = jwtUtil.getUserId(refreshToken);
    String role = jwtUtil.getRole(refreshToken);

    String newAccessToken = jwtUtil.createJwt("access", userId, role, accessTokenExp);

    return new RefreshAccessTokenResponseDTO(newAccessToken);
  }
}
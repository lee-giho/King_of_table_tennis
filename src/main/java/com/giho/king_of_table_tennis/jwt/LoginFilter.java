package com.giho.king_of_table_tennis.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.giho.king_of_table_tennis.dto.CustomUserDetails;
import com.giho.king_of_table_tennis.dto.LoginDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  ObjectMapper objectMapper = new ObjectMapper();

  private final AuthenticationManager authenticationManager;
  private final JWTUtil jwtUtil;
  private final long accessTokenExp;
  private final long refreshTokenExp;

  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getParameter("id"); // 기본인 username을 id로 바꿈
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    ServletInputStream inputStream;
    String requestBody;
    try {
      inputStream = request.getInputStream();
      requestBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    LoginDTO loginDTO;
    try {
      loginDTO = objectMapper.readValue(requestBody, LoginDTO.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    // spring security에서 username과 password를 검증하기 위해 token에 담아야 함
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getId(), loginDTO.getPassword(), null);

    // token에 담은 검증을 위한 AuthenticationManager로 전달
    return authenticationManager.authenticate(authToken);
  }

  // 로그인 성공 시 실행하는 메소드 (여기서 JWT 발급)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {

    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

    String id = customUserDetails.getUsername(); // id를 가져옴

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();

    String accessToken = jwtUtil.createJwt("access", id, role, accessTokenExp);
    String refreshToken = jwtUtil.createJwt("refresh", id, role, refreshTokenExp);

    // JSON 응답
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    Map<String, String> tokens = new HashMap<>();
    tokens.put("accessToken", accessToken);
    tokens.put("refreshToken", refreshToken);

    String responseBody = new ObjectMapper().writeValueAsString(tokens);
    response.getWriter().write(responseBody);
    response.setStatus(HttpStatus.OK.value());
  }

  // 로그인 실패 시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(401);
  }
}

package com.giho.king_of_table_tennis.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

    // 클라이언트 요청에서 username, password 추출
    String username = obtainUsername(request);
    String password = obtainPassword(request);

    // spring security에서 username과 password를 검증하기 위해 token에 담아야 함
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

    // token에 담은 검증을 위한 AuthenticationManager로 전달
    return authenticationManager.authenticate(authToken);
  }

  // 로그인 성공 시 실행하는 메소드 (여기서 JWT 발급)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    System.out.println("로그인 성공");
  }

  // 로그인 실패 시 실행하는 메소드
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    System.out.println("로그인 실패");
  }
}

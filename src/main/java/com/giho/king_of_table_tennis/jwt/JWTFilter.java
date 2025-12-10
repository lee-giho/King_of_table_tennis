package com.giho.king_of_table_tennis.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.giho.king_of_table_tennis.dto.CustomUserDetails;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.exception.ErrorCode;
import com.giho.king_of_table_tennis.exception.ErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

  private final JWTUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    // request에서 Authorization 헤더를 가져옴
    String authorization = request.getHeader("Authorization");

    // Authorization 헤더 X -> 익명으로 진행
    if (authorization == null) {
      System.out.println("token null");
      filterChain.doFilter(request, response);
      return;
    }

    // 형식이 Bearer 가 아니면 -> 잘못된 토큰 형식 에러 응답
    if (!authorization.startsWith("Bearer ")) {
      System.out.println("token invalid format");
      writeError(response, ErrorCode.INVALID_TOKEN_FORMAT, request);
      return;
    }

    // Bearer 부분 제거
    String token = jwtUtil.getTokenWithoutBearer(authorization);

    // 토큰 만료 시 -> 토큰 만료 에러 응답
    if (jwtUtil.isExpired(token)) {
      System.out.println("token expired");
      writeError(response, ErrorCode.TOKEN_EXPIRED, request);
      return;
    }

    // 토큰에서 id와 role 획득
    String id = jwtUtil.getUserId(token);
    String role = jwtUtil.getRole(token);

    // UserEntity 생성
    UserEntity userEntity = new UserEntity();
    userEntity.setId(id);
    userEntity.setPassword("temp_password");
    userEntity.setName("temp_name");
    userEntity.setNickName("temp_nickName");
    userEntity.setEmail("temp_email");
    userEntity.setProfileImage("temp_profileImage");
    userEntity.setRole(role);

    // UserDetails에 UserEntity 객체 담기
    CustomUserDetails customUserDetails = new CustomUserDetails(userEntity);

    // spring security 인증 토큰 생성
    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

    // 세션에 사용자 등록
    SecurityContextHolder.getContext().setAuthentication(authToken);

    // 정상 인증된 상태로 다음 필터/DispatcherServlet 진행
    filterChain.doFilter(request, response);
  }

  private void writeError(HttpServletResponse response,
                          ErrorCode errorCode,
                          HttpServletRequest request) throws IOException {
    response.setStatus(errorCode.getStatus().value());
    response.setContentType("application/json;charset=UTF-8");

    ErrorResponse errorResponse = new ErrorResponse(
      errorCode.name(),
      errorCode.getMessage(),
      LocalDateTime.now(),
      request.getRequestURI()
    );

    ObjectMapper objectMapper = new ObjectMapper();
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}



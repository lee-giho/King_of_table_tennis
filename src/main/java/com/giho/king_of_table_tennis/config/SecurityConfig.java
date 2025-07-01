package com.giho.king_of_table_tennis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http
      .csrf(AbstractHttpConfigurer::disable) // csrf disable
      .formLogin(AbstractHttpConfigurer::disable) // Form 로그인 방식 disable
      .httpBasic(AbstractHttpConfigurer::disable) // http basic 인증 방식 disable
      .authorizeHttpRequests((auth) -> auth // 경로별 인가 작업
        .requestMatchers(
          "/api/auth/**"
        ).permitAll()
        .requestMatchers("/admin").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .sessionManagement((session) -> session // 세션 설정
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
      );

    return http.build();
  }
}

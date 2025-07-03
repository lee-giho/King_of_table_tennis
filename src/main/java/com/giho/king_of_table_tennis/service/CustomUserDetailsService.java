package com.giho.king_of_table_tennis.service;

import com.giho.king_of_table_tennis.dto.CustomUserDetails;
import com.giho.king_of_table_tennis.entity.UserEntity;
import com.giho.king_of_table_tennis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {

    UserEntity userEntity = userRepository.findById(id).orElse(null);

    if (userEntity != null) {
      return new CustomUserDetails(userEntity);
    }

    return null;
  }
}

package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.UserDetailsImpl;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    return new UserDetailsImpl(user);
  }
}

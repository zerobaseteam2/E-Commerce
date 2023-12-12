package com.example.Ecommerce.security;

import com.example.Ecommerce.config.CacheConfig;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;
  
  @Override
  @Cacheable(value = CacheConfig.CacheKey.USER, key = "#username", unless = "#result == null")
  // 캐시에서 먼저 회원을 조회 후 없으면 DB에서 조회
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    return new UserDetailsImpl(user);
  }
}

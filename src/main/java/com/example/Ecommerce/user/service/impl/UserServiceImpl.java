package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.config.CacheConfig;
import com.example.Ecommerce.security.*;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.example.Ecommerce.security.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
  private final JwtTokenUtil jwtTokenUtil;
  
  @Override
  public UserRegisterDto.Response register(UserRegisterDto.Request request) {
    return null;
  }
  
  @Override
  public UserLoginDto.Response login(UserLoginDto.Request request) {
    User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
    checkPassword(request.getPassword(), user.getPassword());
    
    String username = user.getUserId();
    String accessToken = jwtTokenUtil.generateAccessToken(username);
    RefreshToken refreshToken = saveRefreshToken(username);
    return new UserLoginDto.Response(accessToken, refreshToken.getRefreshToken());
  }
  
  @Override
  public UserLoginDto.Response reissue(String refreshToken) {
    refreshToken = resolveToken(refreshToken);
    String username = getCurrentUsername();
    RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username).orElseThrow(NoSuchElementException::new);
    
    if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
      return reissueRefreshToken(refreshToken, username);
    }
    throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
  }
  
  @CacheEvict(value = CacheConfig.CacheKey.USER, key = "#username")
  public void logout(String accessToken2, String username) {
    String accessToken = resolveToken(accessToken2);
    long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
    refreshTokenRedisRepository.deleteById(username);
    logoutAccessTokenRedisRepository.save(LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
  }
  
  private void checkPassword(String rawPassword, String findMemberPassword) {
    if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
      throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }
  }
  
  private RefreshToken saveRefreshToken(String username) {
    return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
            jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
  }
  
  private String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    return principal.getUsername();
  }
  
  private UserLoginDto.Response reissueRefreshToken(String refreshToken, String username) {
    if (lessThanReissueExpirationTimesLeft(refreshToken)) {
      String accessToken = jwtTokenUtil.generateAccessToken(username);
      return new UserLoginDto.Response(accessToken, saveRefreshToken(username).getRefreshToken());
    }
    return new UserLoginDto.Response(jwtTokenUtil.generateAccessToken(username), refreshToken);
  }
  
  private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
    return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
  }
  
  private String resolveToken(String token) {
    return token.substring(7);
  }
}

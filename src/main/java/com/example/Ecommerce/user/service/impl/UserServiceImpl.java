package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.config.CacheConfig;
import com.example.Ecommerce.security.jwt.JwtExpirationEnums;
import com.example.Ecommerce.security.jwt.JwtTokenUtil;
import com.example.Ecommerce.user.domain.LogoutAccessToken;
import com.example.Ecommerce.user.domain.RefreshToken;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.LogoutAccessTokenRedisRepository;
import com.example.Ecommerce.user.repository.RefreshTokenRedisRepository;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;

import static com.example.Ecommerce.security.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;
import static com.example.Ecommerce.security.jwt.JwtTokenUtil.BEARER_PREFIX;

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
    String accessToken = jwtTokenUtil.generateAccessToken(username, user.getRole());
    RefreshToken refreshToken = saveRefreshToken(username);
    return UserLoginDto.Response.of(accessToken, refreshToken.getRefreshToken());
  }
  
  @Override
  public UserLoginDto.Response reissue(String refreshToken) {
    refreshToken = resolveToken(refreshToken);
    RefreshToken redisRefreshToken = refreshTokenRedisRepository.findByRefreshToken(refreshToken).orElseThrow(NoSuchElementException::new);
    
    return reissueRefreshToken(refreshToken, redisRefreshToken.getId());
  }
  
  @CacheEvict(value = CacheConfig.CacheKey.USER, key = "#username")
  public void logout(String accessToken, String username) {
    accessToken = resolveToken(accessToken);
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
  
  private UserLoginDto.Response reissueRefreshToken(String refreshToken, String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new NoSuchElementException("토큰 재발급 오류 : 회원이 없습니다."));
    if (lessThanReissueExpirationTimesLeft(refreshToken)) { // 3일보다 적게 남은 경우 refresh 토큰도 7일로 재발급
      String accessToken = jwtTokenUtil.generateAccessToken(username, user.getRole());
      return new UserLoginDto.Response(accessToken, saveRefreshToken(username).getRefreshToken());
    }
    return new UserLoginDto.Response(jwtTokenUtil.generateAccessToken(username, user.getRole()), refreshToken); // 그게 아닌 경우 refresh 토큰은 재발급하지 않음
  }
  
  private boolean lessThanReissueExpirationTimesLeft(String refreshToken) {
    return jwtTokenUtil.getRemainMilliSeconds(refreshToken) < JwtExpirationEnums.REISSUE_EXPIRATION_TIME.getValue();
  }
  
  private String resolveToken(String token) {
    if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
      return token.replace(BEARER_PREFIX, "");
    }
    return null;
  }
}

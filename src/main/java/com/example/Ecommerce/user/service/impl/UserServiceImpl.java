package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.security.JwtTokenUtil;
import com.example.Ecommerce.security.LogoutAccessTokenRedisRepository;
import com.example.Ecommerce.security.RefreshToken;
import com.example.Ecommerce.security.RefreshTokenRedisRepository;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
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
    UserRole role = user.getRole();
    String accessToken = jwtTokenUtil.generateAccessToken(username, role);
    RefreshToken refreshToken = saveRefreshToken(username);
    return new UserLoginDto.Response(accessToken, refreshToken.getRefreshToken());
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
  
  
}

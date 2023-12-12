package com.example.Ecommerce.user.service;

import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {
  
  public UserRegisterDto.Response registerUser(UserRegisterDto.Request request);
  
  public UserLoginDto.Response login(UserLoginDto.Request request);
  
  public UserLoginDto.Response reissue(String refreshToken);
  
  public void logout(String accessToken, String username);
  
  void verifyUserEmail(Long id);
}

package com.example.Ecommerce.user.service;

import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {
  
  UserRegisterDto.Response registerUser(UserRegisterDto.Request request);
  
  UserLoginDto.Response login(UserLoginDto.Request request);
  
  UserLoginDto.Response reissue(String refreshToken, String username);
  
  void logout(String accessToken, String username);
  
  void verifyUserEmail(Long id);
}

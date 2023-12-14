package com.example.Ecommerce.user.service;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {

  UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

  UserLoginDto.Response login(UserLoginDto.Request request);

  UserLoginDto.Response reissue(String refreshToken);

  void logout(String accessToken, String username);

  void verifyUserEmail(Long id);

  void addUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails);
}

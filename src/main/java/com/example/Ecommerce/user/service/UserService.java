package com.example.Ecommerce.user.service;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserAddressDto.Request;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {
  
  public UserRegisterDto.Response registerUser(UserRegisterDto.Request request);
  
  public UserLoginDto.Response login(UserLoginDto.Request request);
  
  public UserLoginDto.Response reissue(String refreshToken, String username);
  
  public void logout(String accessToken, String username);
  
  void verifyUserEmail(Long id);

  void addUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails);

  void modifyUserAddress(Request request, UserDetailsImpl userDetails, Long deliveryAddressId);

  void deleteUserAddress(UserDetailsImpl userDetails, Long deliveryAddressId);
}

package com.example.Ecommerce.user.service;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserAddressDto.Request;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

  UserLoginDto.Response login(UserLoginDto.Request request);

  UserLoginDto.Response reissue(String refreshToken, String username);

  void logout(String accessToken, String username);

  void verifyUserEmail(Long id);

  void addUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails);

  void modifyUserAddress(Request request, UserDetailsImpl userDetails, Long deliveryAddressId);

  void deleteUserAddress(UserDetailsImpl userDetails, Long deliveryAddressId);

  List<UserAddressDto.Response> getUserAddressList(UserDetailsImpl userDetails, Pageable pageable);
}

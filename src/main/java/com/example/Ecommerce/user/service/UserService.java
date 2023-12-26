package com.example.Ecommerce.user.service;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.dto.FindUserIdDto;
import com.example.Ecommerce.user.dto.FindUserPasswordDto;
import com.example.Ecommerce.user.dto.ResetPasswordDto;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserAddressDto.Request;
import com.example.Ecommerce.user.dto.UserInfoDto;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface UserService {

  UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

  UserLoginDto.Response login(UserLoginDto.Request request);

  UserLoginDto.Response reissue(String refreshToken);

  void logout(String accessToken, String username);

  void verifyUserEmail(Long id);

  void addUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails);

  void modifyUserAddress(Request request, UserDetailsImpl userDetails, Long deliveryAddressId);

  void deleteUserAddress(UserDetailsImpl userDetails, Long deliveryAddressId);

  List<UserAddressDto.Response> getUserAddressList(UserDetailsImpl userDetails, Pageable pageable);

  void setUserRepresentAddress(UserDetailsImpl userDetails, Long deliveryAddressId);

  void modifyUserInfo(UserDetailsImpl userDetails, UserInfoDto.Request request);

  void findUserId(FindUserIdDto.Request request);

  void sendToEmailResetPasswordForm(FindUserPasswordDto.Request request);

  void resetPassword(ResetPasswordDto.Request request, String encryptedUserId);

  void unregisterUser(String accessToken, UserDetailsImpl userDetails);
}

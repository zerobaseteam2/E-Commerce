package com.example.Ecommerce.user.service;

import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {

  UserRegisterDto.Response registerUser(UserRegisterDto.Request request);

  void verifyUserEmail(Long id);
}

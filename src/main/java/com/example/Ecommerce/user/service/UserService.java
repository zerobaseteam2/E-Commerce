package com.example.Ecommerce.user.service;

import com.example.Ecommerce.user.dto.UserRegisterDto;

public interface UserService {

  public UserRegisterDto.Response register(UserRegisterDto.Request request);
}

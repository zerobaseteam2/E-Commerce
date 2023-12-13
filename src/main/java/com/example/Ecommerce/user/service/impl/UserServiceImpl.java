package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserRegisterDto.Response register(UserRegisterDto.Request request) {
    return null;
  }
}

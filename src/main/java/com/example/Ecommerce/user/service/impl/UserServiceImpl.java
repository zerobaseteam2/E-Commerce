package com.example.Ecommerce.user.service.impl;

import com.example.Ecommerce.common.MailComponent;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MailComponent mailComponent;

  @Override
  public UserRegisterDto.Response registerUser(UserRegisterDto.Request request) {
    registerUserDuplicateCheck(request);

    String encryptedPassword = passwordEncoder.encode(request.getPassword());
    User user = userRepository.save(request.toEntity(encryptedPassword));

    mailComponent.sendVerifyLink(user.getId(), user.getEmail(), user.getName());

    return new UserRegisterDto.Response(user.getUserId());
  }

  @Override
  @Transactional
  public void verifyUserEmail(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.verifyUserEmail();
  }

  private void registerUserDuplicateCheck(UserRegisterDto.Request request) {
    if (userRepository.existsByUserId(request.getUserId())) {
      throw new CustomException(ErrorCode.USERID_ALREADY_EXISTS);
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    if (userRepository.existsByPhone(request.getPhone())) {
      throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
    }
  }
}

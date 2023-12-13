package com.example.Ecommerce.user.controller;

import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  public ResponseEntity<UserRegisterDto.Response> registerUser(
      @RequestBody UserRegisterDto.Request request) {
    UserRegisterDto.Response response = userService.register(request);
    return ResponseEntity.ok(response);
  }
}

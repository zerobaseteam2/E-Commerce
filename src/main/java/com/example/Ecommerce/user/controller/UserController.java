package com.example.Ecommerce.user.controller;

import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<UserRegisterDto.Response> registerUser(
      @RequestBody @Valid UserRegisterDto.Request request) {
    UserRegisterDto.Response response = userService.registerUser(request);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/verify/{id}")
  public ResponseEntity verifyUserEmail(@PathVariable Long id) {
    userService.verifyUserEmail(id);

    return ResponseEntity.ok().build();
  }
}

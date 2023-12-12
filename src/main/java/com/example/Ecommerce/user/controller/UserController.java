package com.example.Ecommerce.user.controller;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.security.jwt.JwtTokenUtil;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
  
  private final UserService userService;
  private final JwtTokenUtil jwtTokenUtil;
  
  public ResponseEntity<UserRegisterDto.Response> registerUser(
          @RequestBody UserRegisterDto.Request request) {
    UserRegisterDto.Response response = userService.register(request);
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/login")
  public ResponseEntity<UserLoginDto.Response> login(@RequestBody UserLoginDto.Request request) {
    return ResponseEntity.ok(userService.login(request));
  }
  
  @PostMapping("/reissue")
  public ResponseEntity<UserLoginDto.Response> reissue(@RequestHeader("RefreshToken") String refreshToken) {
    return ResponseEntity.ok(userService.reissue(refreshToken));
  }
  
  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String accessToken, @AuthenticationPrincipal UserDetailsImpl userDetails) {
    String username = userDetails.getUser().getUserId();
    userService.logout(accessToken, username);
  }
}

package com.example.Ecommerce.user.controller;

import static com.example.Ecommerce.security.jwt.JwtTokenUtil.AUTHORIZATION_HEADER;

import com.example.Ecommerce.user.dto.FindUserIdDto;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.dto.FindUserPasswordDto;
import com.example.Ecommerce.user.dto.ResetPasswordDto;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserInfoDto;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
  public ResponseEntity<Void> verifyUserEmail(@PathVariable Long id) {
    userService.verifyUserEmail(id);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid UserLoginDto.Request request) {
    UserLoginDto.Response response = userService.login(request);

    HttpHeaders headers = new HttpHeaders();
    headers.add(AUTHORIZATION_HEADER, response.getAccessToken());
    headers.add("refreshToken", response.getRefreshToken());

    return ResponseEntity.ok().headers(headers).body("login success");
  }

  @PostMapping("/reissue")
  public ResponseEntity<String> reissue(
      @RequestHeader("RefreshToken") String refreshToken) {
    UserLoginDto.Response response = userService.reissue(refreshToken);

    HttpHeaders headers = new HttpHeaders();
    headers.add(AUTHORIZATION_HEADER, response.getAccessToken());
    headers.add("refreshToken", response.getRefreshToken());

    return ResponseEntity.ok().headers(headers).body("login success");
  }

  @PostMapping("/logout")
  public void logout(@RequestHeader("Authorization") String accessToken,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    String username = userDetails.getUser().getUserId();
    userService.logout(accessToken, username);
  }

  @PostMapping("/address")
  public ResponseEntity<Void> addUserAddress(
      @RequestBody @Valid UserAddressDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    userService.addUserAddress(request, userDetails);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/address/{deliveryAddressId}")
  public ResponseEntity<Void> modifyUserAddress(
      @RequestBody @Valid UserAddressDto.Request request,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long deliveryAddressId) {
    userService.modifyUserAddress(request, userDetails, deliveryAddressId);

    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/address/{deliveryAddressId}")
  public ResponseEntity<Void> deleteUserAddress(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long deliveryAddressId) {
    userService.deleteUserAddress(userDetails, deliveryAddressId);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/address")
  public ResponseEntity<List<UserAddressDto.Response>> getUserAddressList(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(userService.getUserAddressList(userDetails, pageable));
  }

  @PutMapping("/address/represent/{deliveryAddressId}")
  public ResponseEntity<Void> setUserRepresentAddress(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long deliveryAddressId) {
    userService.setUserRepresentAddress(userDetails, deliveryAddressId);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/info")
  public ResponseEntity<Void> modifyUserInfo(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid UserInfoDto.Request request) {
    userService.modifyUserInfo(userDetails, request);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/find/userId")
  public ResponseEntity<Void> findUserId(
      @RequestBody @Valid FindUserIdDto.Request request) {
    userService.findUserId(request);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/reset/password")
  public ResponseEntity<Void> sendToEmailResetPasswordForm(
      @RequestBody @Valid FindUserPasswordDto.Request request) {
   userService.sendToEmailResetPasswordForm(request);

    return ResponseEntity.ok().build();
  }

  @PutMapping("/reset/password/{encryptedUserId}")
  public ResponseEntity<Void> resetPassword(
      @RequestBody @Valid ResetPasswordDto.Request request,
      @PathVariable String encryptedUserId) {
    userService.resetPassword(request, encryptedUserId);

    return ResponseEntity.ok().build();
  }
}

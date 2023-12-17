package com.example.Ecommerce.coupon.controller;

import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {
  private final CouponService couponService;
  
//  @PostMapping("/test")
//  public ResponseEntity<Coupon.Response> registerUser(
//          @RequestBody @Valid UserRegisterDto.Request request) {
//    UserRegisterDto.Response response = userService.registerUser(request);
//
//    return ResponseEntity.ok(response);
//  }
}

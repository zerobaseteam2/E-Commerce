package com.example.Ecommerce.coupon.controller;

import com.example.Ecommerce.coupon.dto.*;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupon")
@RequiredArgsConstructor
public class CouponController {
  private final CouponService couponService;
  
  @PostMapping("/issuance")
  public ResponseEntity<CouponIssuanceDto.Response> inssuanceCoupon(
          @RequestBody @Valid CouponIssuanceDto.Request request) {
    CouponIssuanceDto.Response response = couponService.issuanceCoupon(request);
    
    return ResponseEntity.ok(response);
  }
  
  //test용
  @PostMapping("/issuance/birth")
  public ResponseEntity<String> inssuanceBirthDayCoupon() {
    couponService.issuanceBirthDayCoupon();
    
    return ResponseEntity.ok("Success");
  }
  
  
  @PostMapping("/using")
  public ResponseEntity<UseCouponDto.Response> useCoupon(
          @RequestBody @Valid UseCouponDto.Request request) {
    UseCouponDto.Response response = couponService.useCoupon(request);
    
    return ResponseEntity.ok(response);
  }
  //테스트용
  @PostMapping("/expires")
  public ResponseEntity<String> expiresCoupon() {
    couponService.checkExpiredCoupon();
    return ResponseEntity.ok("Success");
  }
  
  @GetMapping("/list")
  public ResponseEntity<PageResponse> viewCoupons(
          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestParam(value = "filter", defaultValue = "ALL", required = false) SearchFilterType filter
          ) {
    PageResponse pageResponse = couponService.viewCoupons(filter, pageNo, userDetails.getUser());
    
    return ResponseEntity.ok(pageResponse);
  }
}

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
  
  @PostMapping("/using")
  public ResponseEntity<UseCouponDto.Response> useCoupon(
          @RequestBody @Valid UseCouponDto.Request request) {
    UseCouponDto.Response response = couponService.useCoupon(request);
    
    return ResponseEntity.ok(response);
  }
  
  @GetMapping("/list")
  public ResponseEntity<PageResponse> viewCoupons(
          @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
          @AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestBody ViewCouponsDto.Request request
          ) {
    PageResponse pageResponse = couponService.viewCoupons(request, pageNo, userDetails.getUser());
    
    return ResponseEntity.ok(pageResponse);
  }
}

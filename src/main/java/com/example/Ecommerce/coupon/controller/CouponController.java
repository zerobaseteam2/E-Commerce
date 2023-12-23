package com.example.Ecommerce.coupon.controller;

import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.dto.PageResponse;
import com.example.Ecommerce.coupon.dto.UseCouponDto;
import com.example.Ecommerce.coupon.dto.ViewCouponsDto;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
      @RequestBody ViewCouponsDto.Request request
  ) {
    PageResponse pageResponse = couponService.viewCoupons(request, pageNo, userDetails.getUser());

    return ResponseEntity.ok(pageResponse);
  }
}

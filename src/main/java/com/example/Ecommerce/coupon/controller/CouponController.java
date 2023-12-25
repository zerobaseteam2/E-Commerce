package com.example.Ecommerce.coupon.controller;

import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.dto.PageResponse;
import com.example.Ecommerce.coupon.dto.SearchFilterType;
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
  public ResponseEntity<CouponIssuanceDto.Response> issuanceCoupon(
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

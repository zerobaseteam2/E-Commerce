package com.example.Ecommerce.coupon.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CouponType {
  MEMBERSHIP_SIGNUP_COUPON("회원가입 기념 쿠폰", 30, 0.1d),
  HAPPY_BIRTHDAY_COUPON("생일 축하 쿠폰", 30, 0.2d);
  
  private final String couponName;
  private final int expirationPeriod;
  private final double discountRate;
}

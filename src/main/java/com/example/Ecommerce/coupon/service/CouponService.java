package com.example.Ecommerce.coupon.service;

import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.dto.UseCouponDto;
import com.example.Ecommerce.coupon.dto.viewCouponsDto;

public interface CouponService {
  CouponIssuanceDto.Response issuanceCoupon(CouponIssuanceDto.Request request);
  
  void issuanceBirthDayCoupon();
  
  UseCouponDto.Response useCoupon(UseCouponDto.Request request);
  
  void checkExpiredCoupon();
  
  viewCouponsDto.Response viewCoupons(viewCouponsDto.Request request);
  
  
}

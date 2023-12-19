package com.example.Ecommerce.coupon.service;

import com.example.Ecommerce.coupon.dto.*;
import com.example.Ecommerce.user.domain.User;

public interface CouponService {
  CouponIssuanceDto.Response issuanceCoupon(CouponIssuanceDto.Request request);
  
  void issuanceBirthDayCoupon();
  
  UseCouponDto.Response useCoupon(UseCouponDto.Request request);
  
  void checkExpiredCoupon();
  
  PageResponse viewCoupons(ViewCouponsDto.Request request, int pageNo, User user);
  
  
}

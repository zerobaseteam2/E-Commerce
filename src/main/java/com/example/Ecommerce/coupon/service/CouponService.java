package com.example.Ecommerce.coupon.service;

import com.example.Ecommerce.coupon.dto.*;
import com.example.Ecommerce.user.domain.User;

public interface CouponService {
  CouponIssuanceDto.Response issuanceCoupon(CouponIssuanceDto.Request request);
  
  void issuanceBirthDayCoupon();
  
  void checkExpiredCoupon();
  
  PageResponse viewCoupons(SearchFilterType filter, int pageNo, User user);
  
  
}

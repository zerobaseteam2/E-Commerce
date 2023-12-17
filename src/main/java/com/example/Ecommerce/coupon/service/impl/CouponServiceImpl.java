package com.example.Ecommerce.coupon.service.impl;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.dto.UseCouponDto;
import com.example.Ecommerce.coupon.dto.viewCouponsDto;
import com.example.Ecommerce.coupon.repository.CouponRepository;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {
  private final CouponRepository couponRepository;
  private final UserRepository userRepository;
  
  // 쿠폰 발급
  @Override
  public CouponIssuanceDto.Response issuanceCoupon(CouponIssuanceDto.Request request) {
    
    // 회원 검증
    User customer = userRepository.findById(request.getCustomerId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    
    LocalDate expirationDate = LocalDate.now().plusDays(request.getCouponType().getExpirationPeriod());
    
    Coupon coupon = couponRepository.save(request.toEntity(expirationDate));
    
    return new CouponIssuanceDto.Response(coupon.getId());
  }
  
  // 생일 기념 쿠폰 발급
  // 스케줄러 설정 필요
  @Override
  public void issuanceBirthDayCoupon() {
    LocalDate nowDate = LocalDate.now();
    
    // 월일이 같은 회원이 조회되는지 확인 필요
    List<User> birthdayUsers = userRepository.findAllByBirth_MonthAndBirth_Day(nowDate);
    
    // 회원 정보를 돌면서 오늘
    for (User birthUser : birthdayUsers) {
      issuanceCoupon(new CouponIssuanceDto.Request()
              .birthDayCouponDto(birthUser));
    }
  }
  
  // 쿠폰 사용
  @Override
  @Transactional
  public UseCouponDto.Response useCoupon(UseCouponDto.Request request) {
    // 쿠폰 id 검증
    Coupon nowCoupon = couponRepository.findById(request.getCouponId()).orElseThrow(() -> new CustomException(ErrorCode.COUPON_NOT_FOUND));
    
    // 만료된 쿠폰인지 확인
    if (nowCoupon.isExpires()) {
      throw new CustomException(ErrorCode.EXPIRES_COUPON);
    }
    
    nowCoupon.useCoupon(request.getOrderDetailNo());
    
    return new UseCouponDto.Response().toDto(nowCoupon);
  }
  
  // 쿠폰 만료여부 확인
  // 스케줄러 설정 필요
  @Override
  public void checkExpiredCoupon() {
  
  }
  
  // 보유 쿠폰 조회
  @Override
  public viewCouponsDto.Response viewCoupons(viewCouponsDto.Request request) {
    return null;
  }
  
  
}

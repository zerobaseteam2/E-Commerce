package com.example.Ecommerce.coupon.service.impl;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.dto.*;
import com.example.Ecommerce.coupon.repository.CouponRepository;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    userRepository.findById(request.getCustomerId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    
    LocalDate expirationDate = LocalDate.now().plusDays(request.getCouponType().getExpirationPeriod());
    
    Coupon coupon = couponRepository.save(request.toEntity(expirationDate));
    
    return new CouponIssuanceDto.Response(coupon.getId());
  }
  
  // 생일 기념 쿠폰 발급
  // 매일 자정 실행
  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 1/1 * ? *")
  public void issuanceBirthDayCoupon() {
    List<User> userList = new ArrayList<>();
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
  
  // 쿠폰 만료 처리
  // 매일 자정 실행
  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 1/1 * ? *")
  public void checkExpiredCoupon() {
    List<Coupon> couponList = couponRepository.findAllByExpiresFalseAndExpirationDateBefore(LocalDate.now());
    for (Coupon coupon : couponList) {
      coupon.couponExpires();
    }
  }
  
  // 보유 쿠폰 조회
  @Override
  public PageResponse viewCoupons(ViewCouponsDto.Request request, int pageNo, User user) {
    // 정렬 기준에 따라 pageable 객체 초기화
    Pageable pageable = createPageable(pageNo);
    
    // 찾아온 데이터 Page 객체
    Page<Coupon> couponPage = filtering(request.getFilter(), pageable, user.getId());
    
    // page 객체에서 쿠폰 리스트를 추출하여 responseDto에 저장
    List<ViewCouponsDto.Response> responseDto = createListResponseDto(couponPage);
    
    // page 객체를 통해 return
    return new PageResponse().toDto(pageNo, couponPage, responseDto);
  }
  
  private Pageable createPageable(int pageNo) {
    return PageRequest.of(pageNo, 10, Sort.by("issuanceDate").descending());
  }
  
  private Page<Coupon> filtering(SearchFilterType filter, Pageable pageable, Long customerId) {
    // 전체 조회
    if (filter == SearchFilterType.ALL) {
      return couponRepository.findAllByCustomerId(pageable, customerId);
    }
    
    // 사용 완료 쿠폰 조회
    if (filter == SearchFilterType.USABLE) {
      return couponRepository.findAllByCustomerIdAndOrderDetailNoNullAndExpiresFalse(pageable, customerId);
    }
    
    // 사용 가능 쿠폰 조회
    if (filter == SearchFilterType.USED) {
      return couponRepository.findAllByCustomerIdAndOrderDetailNoNotNull(pageable, customerId);
    }
    
    // 만료된 쿠폰 조회
    if (filter == SearchFilterType.EXPIRES) {
      return couponRepository.findAllByCustomerIdAndExpiresTrue(pageable, customerId);
    }
    
    throw new CustomException(ErrorCode.FILTER_TYPE_ERROR);
  }
  
  private List<ViewCouponsDto.Response> createListResponseDto(Page<Coupon> postPage) {
    List<Coupon> coupons = postPage.getContent();
    List<ViewCouponsDto.Response> responseDto = new ArrayList<>();
    for (Coupon coupon : coupons) {
      ViewCouponsDto.Response nowCoupon = new ViewCouponsDto.Response().toDto(coupon);
      responseDto.add(nowCoupon);
    }
    
    return responseDto;
  }
  
}

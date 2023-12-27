package com.example.Ecommerce.coupon.service.impl;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.domain.CouponType;
import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.dto.PageResponse;
import com.example.Ecommerce.coupon.dto.SearchFilterType;
import com.example.Ecommerce.coupon.dto.ViewCouponsDto;
import com.example.Ecommerce.coupon.repository.CouponRepository;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

  private final CouponRepository couponRepository;
  private final UserRepository userRepository;

  // 쿠폰 발급
  @Override
  public CouponIssuanceDto.Response issuanceCoupon(CouponIssuanceDto.Request request) {

    // 회원 검증
    User user = userRepository.findById(request.getCustomerId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    LocalDate expirationDate = LocalDate.now()
        .plusDays(request.getCouponType().getExpirationPeriod());

    Coupon coupon = couponRepository.save(request.toEntity(expirationDate));

    return new CouponIssuanceDto.Response(coupon.getId());
  }

  // 생일 기념 쿠폰 발급
  // 매일 자정 실행
  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void issuanceBirthDayCoupon() {
    List<User> userList = userRepository.findByBirthMonthAndDay(LocalDate.now());

    for (User user : userList) {
      issuanceCoupon(
          new CouponIssuanceDto.Request(user.getId(), CouponType.HAPPY_BIRTHDAY_COUPON, null));

    }
  }

  // 쿠폰 만료 처리
  // 매일 자정 실행
  @Override
  @Transactional
  @Scheduled(cron = "0 0 0 * * ?")
  public void checkExpiredCoupon() {
    List<Coupon> couponList = couponRepository.findAllByIsExpiredFalseAndOrderNoNullAndExpirationDateBefore(
        LocalDate.now());
    for (Coupon coupon : couponList) {
      coupon.couponExpires();
    }
  }

  // 보유 쿠폰 조회
  @Override
  public PageResponse viewCoupons(SearchFilterType filterType, int pageNo, User user) {
    // 정렬 기준에 따라 pageable 객체 초기화
    Pageable pageable = createPageable(pageNo);

    // 찾아온 데이터 Page 객체
    Page<Coupon> couponPage = filtering(filterType, pageable, user.getId());

    // page 객체에서 쿠폰 리스트를 추출하여 responseDto에 저장
    List<ViewCouponsDto.Response> responseDto = createListResponseDto(couponPage);

    // page 객체를 통해 return
    return new PageResponse().toDto(pageNo, couponPage, responseDto);
  }

  @Override
  public PageResponse viewCouponsForAdmin(SearchFilterType filterType, int pageNo, Long customerId) {
    // 정렬 기준에 따라 pageable 객체 초기화
    Pageable pageable = createPageable(pageNo);

    // 찾아온 데이터 Page 객체
    Page<Coupon> couponPage = filtering(filterType, pageable, customerId);

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

    // 사용 가능 쿠폰 조회
    if (filter == SearchFilterType.USABLE) {
      return couponRepository.findAllByCustomerIdAndOrderNoNullAndIsExpiredFalse(pageable,
          customerId);
    }

    // 사용 완료 쿠폰 조회
    if (filter == SearchFilterType.USED) {
      return couponRepository.findAllByCustomerIdAndOrderNoNotNull(pageable, customerId);
    }

    // 만료된 쿠폰 조회
    if (filter == SearchFilterType.EXPIRES) {
      return couponRepository.findAllByCustomerIdAndIsExpiredTrue(pageable, customerId);
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

package com.example.Ecommerce.coupon.repository;

import com.example.Ecommerce.coupon.domain.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
  Optional<Coupon> findById(Long couponId);
  
  // 전체 쿠폰 조회
  Page<Coupon> findAllByCustomerId(Pageable pageable, Long customerId);
  
  // 사용 완료 쿠폰 조회
  Page<Coupon> findAllByCustomerIdAndOrderDetailNoNotNull(Pageable pageable, Long customerId);
  
  // 사용 가능 쿠폰 조회
  Page<Coupon> findAllByCustomerIdAndOrderDetailNoNullAndExpiresFalse(Pageable pageable, Long customerId);
  
  // 만료된 쿠폰 조회
  Page<Coupon> findAllByCustomerIdAndExpiresTrue(Pageable pageable, Long customerId);
}

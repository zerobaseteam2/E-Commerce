package com.example.Ecommerce.coupon.repository;

import com.example.Ecommerce.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
  Optional<Coupon> findById(Long couponId);
}

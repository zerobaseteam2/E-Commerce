package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.coupon.domain.Coupon;
import com.example.Ecommerce.coupon.domain.CouponType;
import com.example.Ecommerce.user.domain.User;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class CouponIssuanceDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private Long customerId;
    private CouponType couponType;
    private String category;

    public Request birthDayCouponDto(User user) {
      return Request.builder()
          .customerId(user.getId())
          .couponType(CouponType.HAPPY_BIRTHDAY_COUPON)
          .build();
    }

    public Coupon toEntity(LocalDate expirationDate) {
      return Coupon.builder()
          .customerId(customerId)
          .couponName(couponType.getCouponName())
          .usableCategory(category)
          .discountRate(couponType.getDiscountRate())
          .expirationDate(expirationDate)
          .expires(false)
          .build();
    }
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long couponId;
  }
}

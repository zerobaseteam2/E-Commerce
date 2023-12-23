package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.coupon.domain.Coupon;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UseCouponDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {

    @NotNull
    private Long couponId;
    @NotNull
    private Long orderNo;
  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {

    private Long couponId;
    private Long orderNo;
    private String couponName;
    private double discountRate;

    public Response toDto(Coupon coupon) {
      return Response.builder()
              .couponId(coupon.getId())
              .orderNo(coupon.getOrderNo())
              .couponName(coupon.getCouponName())
              .discountRate(coupon.getDiscountRate())
              .build();
    }

  }
}

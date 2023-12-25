package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.coupon.domain.Coupon;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ViewCouponsDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private Long couponId;
    private Long customerId;
    private Long orderDetailNo;
    private String couponName;
    private String usableCategory;
    private double discountRate;
    private LocalDate issuanceDate;
    private LocalDate expirationDate;

    public Response toDto(Coupon coupon) {
      return Response.builder()
          .couponId(coupon.getId())
          .customerId(coupon.getCustomerId())
          .orderDetailNo(coupon.getOrderNo())
          .couponName(coupon.getCouponName())
          .discountRate(coupon.getDiscountRate())
          .issuanceDate(coupon.getIssuanceDate())
          .expirationDate(coupon.getExpirationDate())
          .build();
    }
  }
}
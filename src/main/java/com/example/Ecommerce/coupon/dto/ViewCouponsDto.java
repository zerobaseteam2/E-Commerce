package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.coupon.domain.Coupon;
import lombok.*;

import java.time.LocalDate;

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
              .usableCategory(coupon.getUsableCategory())
              .discountRate(coupon.getDiscountRate())
              .issuanceDate(coupon.getIssuanceDate())
              .expirationDate(coupon.getExpirationDate())
              .build();
    }
  }
}

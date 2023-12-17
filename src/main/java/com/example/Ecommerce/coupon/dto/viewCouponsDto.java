package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.coupon.domain.CouponType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class viewCouponsDto {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotBlank
    private Long customerId;
    @NotBlank
    private String filter;
  }
  
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String message;
  }
}

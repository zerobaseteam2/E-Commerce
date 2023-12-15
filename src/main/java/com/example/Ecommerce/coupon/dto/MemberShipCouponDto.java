package com.example.Ecommerce.coupon.dto;

import com.example.Ecommerce.user.dto.UserLoginDto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import static com.example.Ecommerce.security.jwt.JwtTokenUtil.BEARER_PREFIX;

public class MemberShipCouponDto {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String userId;
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
  }
  
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response {
    private String accessToken;
    private String refreshToken;
    
    public static UserLoginDto.Response of(String accessToken, String refreshToken) {
      return UserLoginDto.Response.builder()
              .accessToken(accessToken)
              .refreshToken(BEARER_PREFIX + refreshToken)
              .build();
    }
  }
}

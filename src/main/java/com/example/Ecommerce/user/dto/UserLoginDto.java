package com.example.Ecommerce.user.dto;

import static com.example.Ecommerce.security.jwt.JwtTokenUtil.BEARER_PREFIX;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserLoginDto {
  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request {
    private String userId;
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
    
    public static Response of(String accessToken, String refreshToken) {
      return Response.builder()
              .accessToken(accessToken)
              .refreshToken(BEARER_PREFIX + refreshToken)
              .build();
    }
  }
}

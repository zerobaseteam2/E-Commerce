package com.example.Ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import static com.example.Ecommerce.security.JwtTokenUtil.BEARER_PREFIX;

public class UserLoginDto {
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  public static class Request {
    private String username;
    private String password;
  }
  
  @Getter
  @Setter
  @Builder
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

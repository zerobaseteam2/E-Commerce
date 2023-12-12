package com.example.Ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserRegisterDto {
  
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  public static class Request {
    private Long number;
  }
  
  @Getter
  @Setter
  @Builder
  @AllArgsConstructor
  public static class Response {
  }
}

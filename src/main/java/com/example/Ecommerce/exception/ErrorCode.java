package com.example.Ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "Not found."),
  USERID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "UserID already exists."),
  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Email already exists."),
  PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Phone Number already exists."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found."),
  COUPON_NOT_FOUND(HttpStatus.BAD_REQUEST, "Coupon not found."),
  EXPIRES_COUPON(HttpStatus.BAD_REQUEST, "Coupon has expired."),
  FILTER_TYPE_ERROR(HttpStatus.BAD_REQUEST, "The filter type is incorrect.");
  
  private HttpStatus status;
  private String message;
}

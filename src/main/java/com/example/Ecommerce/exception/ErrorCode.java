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

  // 로그인
  REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "Refresh token has not been entered."),
  REFRESH_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "Refresh token does not exist."),
  REFRESH_TOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "Refresh token has expired."),

  // 판매자 exception
  SELLING_PRODUCT_NOT_EXIST(HttpStatus.BAD_REQUEST,"판매하는 상품이 없습니다."),

  COUPON_NOT_FOUND(HttpStatus.BAD_REQUEST, "Coupon not found."),
  EXPIRES_COUPON(HttpStatus.BAD_REQUEST, "Coupon has expired."),
  FILTER_TYPE_ERROR(HttpStatus.BAD_REQUEST, "The filter type is incorrect."),

  // 관리자 상품 요청 승인 거절관련 exception
  PRODUCT_NOT_WAITING(HttpStatus.BAD_REQUEST, "승인 대기중인 상품이 아닙니다."),
  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 상품을 찾을수 없습니다."),
  DELIVERY_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "Address not found"),
  
  // 리뷰 excepion
  REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "리뷰를 찾을 수 없습니다."),

  // 수정이 필요한 예외 처리.
  INVALID_OPERATION(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다."),
  UN_AUTHORIZED(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다.");
  
  
  private final HttpStatus status;
  private final String message;
}

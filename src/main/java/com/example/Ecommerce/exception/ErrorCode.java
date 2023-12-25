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
  INVALID_JWT_SIGNATURE(HttpStatus.FORBIDDEN, "유효하지 않는 JWT 서명입니다."),
  EXPIRED_JWT_TOKEN(HttpStatus.FORBIDDEN, "만료된 JWT 토큰입니다."),
  UNSUPPORTED_JWT_TOKEN(HttpStatus.FORBIDDEN, "지원되지 않는 JWT 토큰입니다."),
  JWT_CLAIMS_IS_EMPTY(HttpStatus.FORBIDDEN, "잘못된 JWT 토큰입니다."),

  // 판매자 exception
  SELLING_PRODUCT_NOT_EXIST(HttpStatus.BAD_REQUEST,"판매하는 상품이 없습니다."),
  PRODUCT_NOT_APPROVED(HttpStatus.BAD_REQUEST,"판매 승인된 상품이 아닙니다."),
  PRODUCT_OPTION_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 상품의 옵션을 찾을 수 없습니다."),
  PRODUCT_TAG_NOT_FOUND(HttpStatus.BAD_REQUEST,"해당 상품의 태그를 찾을 수 없습니다."),
  ALREADY_EXIST_OPTION(HttpStatus.BAD_REQUEST,"이미 존재하는 상품 옵션명입니다."),

  // 관리자 상품 요청 승인 거절관련 exception
  PRODUCT_NOT_WAITING(HttpStatus.BAD_REQUEST, "승인 대기중인 상품이 아닙니다."),
  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 상품을 찾을수 없습니다."),
  PRODUCT_NOT_CANCEL_REQUEST(HttpStatus.BAD_REQUEST, "물품 등록 취소 혹은 판매 중지 요청한 상품이 아닙니다."),
  DELIVERY_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "Address not found"),

  // 검색 exception
  SEARCH_NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "검색하신 상품을 찾을수 없습니다."),


  EXPIRES_COUPON(HttpStatus.BAD_REQUEST, "Coupon has expired."),
  FILTER_TYPE_ERROR(HttpStatus.BAD_REQUEST, "The filter type is incorrect."),


  // 리뷰 관련
  NOT_PURCHASE_CONFIRMED(HttpStatus.BAD_REQUEST, "구매 확정 상태가 아닙니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
  REPLY_EXISTS(HttpStatus.BAD_REQUEST, "이미 답변이 등록된 리뷰입니다."),

  NOT_SELLER_OF_THE_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품의 판매자가 아닙니다."),

  // 수정이 필요한 예외 처리.
  INVALID_OPERATION(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다."),
  INVALID_INPUT(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다."),
  UN_AUTHORIZED(HttpStatus.BAD_REQUEST, "예외처리 수정이 필요합니다."),

  // 쿠폰 관련
  COUPON_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용하려는 쿠폰이 존재하지 않습니다."),
  COUPON_IS_EXPIRED(HttpStatus.BAD_REQUEST, "만료된 쿠폰입니다."),
  USED_COUPON(HttpStatus.BAD_REQUEST, "이미 사용된 쿠폰입니다."),


  // 주문 관련
  ORDER_PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 상품주문정보가 없습니다."),
  ORDER_NOT_FOUND(HttpStatus.BAD_REQUEST,"주문이 존재하지 않습니다."),
  INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "상품 수량은 1개 이상으로 변경 가능합니다."),

  // 게시글 관련
  POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다."),
  COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글이 존재하지 않습니다." );


  private final HttpStatus status;
  private final String message;
}

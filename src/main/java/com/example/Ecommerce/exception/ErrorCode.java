package com.example.Ecommerce.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "찾을 수 없습니다."),
  USERID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
  PHONE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 전화번호입니다."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "회원을 찾을 수 없습니다."),


  // 로그인
  REFRESH_TOKEN_NULL(HttpStatus.BAD_REQUEST, "재발급 토큰이 존재하지 않습니다"),
  REFRESH_TOKEN_NOT_EXIST(HttpStatus.BAD_REQUEST, "재발급 토큰을 찾을 수 없습니다."),
  REFRESH_TOKEN_HAS_EXPIRED(HttpStatus.BAD_REQUEST, "재발급 유효기간이 지난 토큰입니다."),
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
  DELIVERY_ADDRESS_NOT_FOUND(HttpStatus.BAD_REQUEST, "배송지를 찾을 수 없습니다."),

  // 검색 exception
  SEARCH_NOT_FOUND_PRODUCT(HttpStatus.BAD_REQUEST, "검색하신 상품을 찾을수 없습니다."),


  EXPIRES_COUPON(HttpStatus.BAD_REQUEST, "유효기간이 지난 쿠폰입니다."),
  FILTER_TYPE_ERROR(HttpStatus.BAD_REQUEST, "필터 타입이 올바르지 않습니다"),


  // 리뷰 관련
  NOT_PURCHASE_CONFIRMED(HttpStatus.BAD_REQUEST, "구매 확정 상태가 아닙니다."),
  REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
  REPLY_EXISTS(HttpStatus.BAD_REQUEST, "이미 답변이 등록된 리뷰입니다."),

  NOT_SELLER_OF_THE_PRODUCT(HttpStatus.BAD_REQUEST, "해당 상품의 판매자가 아닙니다."),

  // 문의
  INQUIRY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 문의내역이 없습니다."),
  YOU_HAVE_NO_PERMISSION(HttpStatus.FORBIDDEN, "해당 권한이 없습니다."),
  INQUIRY_REPLY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 답변이 존재하지 않습니다."),
  EXIST_REPLY(HttpStatus.BAD_REQUEST, "답변이 이미 존재합니다."),
  CONTENT_CAN_NOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "관리자 답변이 작성되어 내용 수정이 불가합니다."),

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
  INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "상품 수량은 1개 이상이어야 합니다."),
  NO_INVENTORY(HttpStatus.BAD_REQUEST, "재고가 없습니다. 판매자에게 문의해 주세요."),

  // 게시글 관련
  POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "게시글이 존재하지 않습니다."),
  COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "댓글이 존재하지 않습니다." );


  private final HttpStatus status;
  private final String message;
}

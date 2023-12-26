package com.example.Ecommerce.product.dto.seller;

import lombok.Getter;

@Getter
public enum ProductConfirm {
  WAITING("승인대기중"),
  REQUEST_CANCEL_REGISTER("등록 취소 요청"),
  REQUEST_CANCEL_SELLING("판매 중지 요청"),
  APPROVED_CANCEL_REGISTER("등록 취소 승인"),
  APPROVED_CANCEL_SELLING("판매 중지 승인"),
  APPROVED("승인됨"),
  NOT_APPROVED("승인거절됨");

  private final String confirm;

  ProductConfirm(String confirm) {
    this.confirm = confirm;
  }

}

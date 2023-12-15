package com.example.Ecommerce.product.dto;

import lombok.Getter;

@Getter
public enum ProductConfirm {
  WAITING("승인대기중"),
  APPROVED("승인됨"),
  NOT_APPROVED("승인거절됨");

  private final String confirm;

  ProductConfirm(String confirm) {
    this.confirm = confirm;
  }

}

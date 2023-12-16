package com.example.Ecommerce.product.dto;

public enum ProductState {

  FOR_SALE("판매중"),
  ON_SALE("할인중"),
  STOP_SELLING("판매 중지"),
  NO_STOCK("품절됨");

  private final String state;

  ProductState(String state) {
    this.state = state;
  }


}

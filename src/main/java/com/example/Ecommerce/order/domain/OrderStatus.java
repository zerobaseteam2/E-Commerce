package com.example.Ecommerce.order.domain;

public enum OrderStatus {

  // 구매자 상태변경 가능
  ORDER_CANCELED, //취소완료
  PURCHASE_CONFIRMED, //구매확정
  EXCHANGE_REQUESTED, //교환신청
  REFUND_REQUESTED, //환불신청

  // 시스템 상태변경 가능
  ORDER_COMPLETE, //주문완료
  SHIPPING, //배송중
  SHIPPING_COMPLETE, //배송완료

  // 판매자 변경가능
  RETURN_COMPLETE, //반품완료
  EXCHANGE_COMPLETE,//교환완료
  REFUND_COMPLETE //환불완료
}

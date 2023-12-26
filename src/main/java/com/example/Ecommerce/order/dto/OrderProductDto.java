package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {


  private Long orderProductId; //주문상품 id
  private Long orderId; //주문 id
  private Long productId; //상품 id
  private String productName; //상품명
  private int productPrice; //상품금액
  private int quantity; //상품수량
  private OrderStatus status; //주문상태

  // OrderProduct entity -> OrderProduct dto 변경하여 반환
  public static OrderProductDto of(OrderProduct orderProduct) {
    return OrderProductDto.builder()
        .orderProductId(orderProduct.getId())
        .orderId(orderProduct.getOrder().getId())
        .productId(orderProduct.getProduct().getId())
        .productName(orderProduct.getProduct().getName())
        .productPrice(orderProduct.getProduct().getPrice())
        .quantity(orderProduct.getQuantity())
        .status(orderProduct.getStatus())
        .build();
  }

}

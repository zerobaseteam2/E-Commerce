package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.OrderProductOption;
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
public class OrderProductOptionDto {

  private Long orderProductOptionId; //주문상품옵션 id
  private Long productOptionId; //상품옵션 id
  private String productOptionName; //상품옵션 이름
  private Integer quantity; //주문상품 수량

  public static OrderProductOptionDto  of(OrderProductOption orderProductOption) {
    return OrderProductOptionDto.builder()
        .orderProductOptionId(orderProductOption.getId())
        .productOptionId(orderProductOption.getProductOptionId())
        .productOptionName(orderProductOption.getProductOptionName())
        .quantity(orderProductOption.getQuantity())
        .build();
  }
}

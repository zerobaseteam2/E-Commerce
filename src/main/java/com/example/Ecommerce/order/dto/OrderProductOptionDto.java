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

  private Long orderProductOptionId;
  private String orderProductOptionName;
  private Integer quantity;

  public static OrderProductOptionDto  of(OrderProductOption orderProductOption) {
    return OrderProductOptionDto.builder()
        .orderProductOptionId(orderProductOption.getId())
        .quantity(orderProductOption.getQuantity())
        .orderProductOptionName(orderProductOption.getOrderProduct().getProduct().getName())
        .build();
  }
}

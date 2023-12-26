package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderProductOption;
import com.example.Ecommerce.order.domain.OrderStatus;
import java.util.List;
import java.util.stream.Collectors;
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
public class OrderProductDetailDto {


  private Long orderProductId; //주문상품 id
  private Long orderId; //주문 id
  private Long productId; //상품 id
  private String productName; //상품명
  private Integer productPrice; //상품금액
  private List<OrderProductOptionDto> orderProductOptionList; //옵션, 상품수량 정보
  private OrderStatus status; //주문상태

  // OrderProduct entity -> OrderProduct dto 변경하여 반환
  public static OrderProductDetailDto of(OrderProduct orderProduct) {
    return OrderProductDetailDto.builder()
        .orderProductId(orderProduct.getId())
        .orderId(orderProduct.getOrder().getId())
        .productId(orderProduct.getProduct().getId())
        .productName(orderProduct.getProduct().getName())
        .productPrice(orderProduct.getProduct().getPrice())
        .orderProductOptionList(getOrderProductDtos(orderProduct.getOrderProductOptionList()))
        .status(orderProduct.getStatus())
        .build();
  }

  // OrderProductOption list -> OrderProductOptionDto list 변경
  private static List<OrderProductOptionDto> getOrderProductDtos(List<OrderProductOption> orderProductOptionList) {
    return orderProductOptionList.stream()
        .map(OrderProductOptionDto::of)
        .collect(Collectors.toList());
  }

}

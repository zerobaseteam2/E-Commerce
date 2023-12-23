package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.order.domain.OrderProduct;
import java.time.LocalDate;
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
public class OrderDetailDto {

  private Long id;
  private String recipientName;
  private String recipientPhone;
  private LocalDate orderAt;
  private LocalDate updatedAt;
  private Long zoneNo;
  private String roadAddress;
  private String detailedAddress;
  private Long couponId;
  private int totalDiscountPrice;
  private int totalPaymentPrice;

  private List<OrderProductDto> orderProductList;


  // Order entity -> OrderDetailDto 변경하여 반환
  public static OrderDetailDto of(Order order) {
    return OrderDetailDto.builder()
        .id(order.getId())
        .recipientName(order.getRecipientName())
        .recipientPhone(order.getRecipientPhone())
        .orderAt(order.getOrderAt())
        .updatedAt(order.getUpdatedAt())
        .zoneNo(order.getZoneNo())
        .roadAddress(order.getRoadAddress())
        .detailedAddress(order.getDetailedAddress())
        .couponId(order.getCouponId())
        .totalDiscountPrice(order.getTotalDiscountPrice())
        .totalPaymentPrice(order.getTotalPaymentPrice())
        .orderProductList(getOrderProductDtos(order.getOrderProductList()))
        .build();
  }


  // OrderProduct list -> OrderProductDto list 변경
  private static List<OrderProductDto> getOrderProductDtos(List<OrderProduct> orderProductList) {
    return orderProductList.stream()
        .map(OrderProductDto::of)
        .collect(Collectors.toList());
  }
}





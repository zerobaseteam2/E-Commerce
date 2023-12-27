package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.OrderStatus;
import com.example.Ecommerce.order.domain.OrderStatusHistory;
import java.time.LocalDate;
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
public class OrderStatusHistoryDto {

  private Long id;
  private OrderStatus previousStatus; //이전 상태
  private OrderStatus currentStatus; //변경 상태
  private LocalDate updatedAt; //수정날짜

  public static OrderStatusHistoryDto of(OrderStatusHistory orderStatusHistory){
    return OrderStatusHistoryDto.builder()
        .id(orderStatusHistory.getId())
        .previousStatus(orderStatusHistory.getPreviousStatus())
        .currentStatus(orderStatusHistory.getCurrentStatus())
        .updatedAt(orderStatusHistory.getUpdatedAt())
        .build();
  }

}

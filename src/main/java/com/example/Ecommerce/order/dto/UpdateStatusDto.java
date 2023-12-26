package com.example.Ecommerce.order.dto;

import com.example.Ecommerce.order.domain.OrderProduct;
import com.example.Ecommerce.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UpdateStatusDto {

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Request{

    @NotNull
    private Long orderProductId; //주문상품 id
    @NotNull
    private OrderStatus orderStatus; //변경하려는 상태

  }

  @Getter
  @Setter
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Response{
    @NotNull
    private Long orderProductId; //주문상품 id
    @NotNull
    private OrderStatus updatedStatus; //변경된 상태

    // OrderProduct entity -> UpdateStatus dto
    public static Response of(OrderProduct orderProduct) {
      return Response.builder()
          .orderProductId(orderProduct.getId())
          .updatedStatus(orderProduct.getStatus())
          .build();
    }
  }

}

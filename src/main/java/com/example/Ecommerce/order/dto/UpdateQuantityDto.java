package com.example.Ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;
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
public class UpdateQuantityDto {

  @NotNull
  private Long orderProductOptionId; //주문상품옵션 id
  @NotNull
  private Integer quantity; //수량
}



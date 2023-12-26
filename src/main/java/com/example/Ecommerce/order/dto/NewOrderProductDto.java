package com.example.Ecommerce.order.dto;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
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
public class NewOrderProductDto {

  @NotNull
  private Long productId; //상품 id
  private Long optionId; //옵션 id
  @NotNull
  private Integer quantity; //주문 수량
}
